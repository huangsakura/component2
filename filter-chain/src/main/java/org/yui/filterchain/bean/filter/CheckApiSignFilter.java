package org.yui.filterchain.bean.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.yui.base.bean.api.JsonResult;
import org.yui.base.bean.constant.DocAccessTokenConstant;
import org.yui.base.bean.constant.StringConstant;
import org.yui.base.exception.BusinessException;
import org.yui.filterchain.bean.constant.FilterChainConstant;
import org.yui.filterchain.config.properties.FilterChainProperties;
import org.yui.safety.md5.Md5Util;
import org.yui.spring.util.EnvUtil;
import org.yui.tomcat.bean.filter.wrapper.PutStringToBodyWrapper;
import org.yui.tomcat.util.ServletResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.core.env.Environment;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yui.tomcat.util.ServletRequestUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * @author huangjinlong
 * @time 2019-04-09 17:48
 * @description
 *
 * 这个过滤器一定要在 web模块的UrlDecodeFilter之前，否则会有问题
 */
public class CheckApiSignFilter extends OncePerRequestFilter {

    private RedissonClient redissonClient;

    private FilterChainProperties filterChainProperties;

    private Environment environment;

    /**
     * 误差的毫秒数,5分钟
     */
    private static final long DEVIATION_MILLISECONDS = 300000;

    /**
     *
     */
    private static volatile JsonResult INVALID_REQUEST = null;

    /**
     * 签名随机数的redis key的前缀
     */
    private static final String SIGN_NONCE_REDIS_KEY_PREFIX = "SIGN_NONCE_";

    /**
     * nonce在redis中存在的时长,单位：分钟
     */
    private static final long SIGN_NONCE_LIVE_MINUTES = 10;

    /**
     * 随机数的最小长度
     */
    private static final int SIGN_NONCE_MIN_LENGTH = 13;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 单例模式
     */
    private static void singletonInvalidRequest() {

        if (null == INVALID_REQUEST) {
            synchronized (CheckApiSignFilter.class) {
                if (null == INVALID_REQUEST) {
                    INVALID_REQUEST = new JsonResult();
                    INVALID_REQUEST.setCode("INVALID_REQUEST");
                }
            }
        }
    }

    public CheckApiSignFilter(RedissonClient redissonClient,FilterChainProperties filterChainProperties,Environment environment) {
        this.redissonClient = redissonClient;
        this.filterChainProperties = filterChainProperties;
        this.environment = environment;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * 判断当前uri是否需要校验接口签名
         */
        if (!needCheckApiSign(request)) {
            filterChain.doFilter(request,response);
            return;
        }

        /**
         * 获取body中的字符串
         */
        String bodyString = ServletRequestUtil.getStringFromBody(request);

        JSONObject bodyJson = null;
        try {
            bodyJson = JSONObject.parseObject(bodyString);
        } catch (Exception e) {
            logger.error("json:"+bodyString);
            throw new BusinessException("JSON_PARSE_ERROR", "JSON解析失败",false);
        }

        /**
         * 获取时间戳
         */
        String timestamp = getValueFromBody(bodyJson, FilterChainConstant.TIMESTAMP);
        if (StringUtils.isBlank(timestamp)) {
            doSthBeforeReturn(FilterChainConstant.TIMESTAMP + "参数缺失",response);
            return;
        }

        /**
         * 获取签名
         */
        String sign = getValueFromBody(bodyJson,FilterChainConstant.SIGN);
        if (StringUtils.isBlank(sign)) {
            doSthBeforeReturn(FilterChainConstant.SIGN + "参数缺失",response);
            return;
        }

        /**
         * 获取随机数字符串
         */
        String nonce = getValueFromBody(bodyJson,FilterChainConstant.NONCE);
        if (StringUtils.isBlank(nonce) || (nonce.length() < SIGN_NONCE_MIN_LENGTH)) {
            doSthBeforeReturn(FilterChainConstant.NONCE + "参数缺失或长度小于" + SIGN_NONCE_MIN_LENGTH
                    ,response);
            return;
        }

        /**
         * 获取app_key
         */
        String appKey = getValueFromBody(bodyJson,FilterChainConstant.APP_KEY);
        if (StringUtils.isBlank(appKey)) {
            doSthBeforeReturn(FilterChainConstant.APP_KEY + "参数缺失",response);
            return;
        }

        /**
         * 尝试获取app_secret
         * 如果存在的话，反而是有问题的。不应该传输此参数
         */
        String appSecret = getValueFromBody(bodyJson,FilterChainConstant.APP_SECRET);
        if (StringUtils.isNotBlank(appSecret)) {
            doSthBeforeReturn(FilterChainConstant.APP_SECRET + "参数不能传输",response);
            return;
        }

        /**
         * 校验时间戳
         */
        long now = System.currentTimeMillis();
        long pastTime = now - DEVIATION_MILLISECONDS;
        long futureTime = now + DEVIATION_MILLISECONDS;
        long requestTimestamp = Long.parseLong(timestamp);

        if ((requestTimestamp < pastTime) || (requestTimestamp > futureTime)) {
            doSthBeforeReturn("请求已失效,服务器的时间戳为"+now,response);
            return;
        }

        /**
         * 校验随机数
         */
        RBucket<Object> rBucket = redissonClient.getBucket(
                SIGN_NONCE_REDIS_KEY_PREFIX + appKey + StringConstant.UNDERLINE + nonce);
        if (null != rBucket.get()) {
            doSthBeforeReturn("同一个appKey在" + SIGN_NONCE_LIVE_MINUTES + "分钟内存在其他的"+
                    FilterChainConstant.NONCE+"参数值为"+nonce+"的请求",response);
            return;
        }
        rBucket.set(StringConstant.ZERO,SIGN_NONCE_LIVE_MINUTES, TimeUnit.MINUTES);

        /**
         * 校验md5
         */
        appSecret = RequestInfoHolder.getAppSecret();

        if (StringUtils.isBlank(appSecret)) {
            doSthBeforeReturn("根据"+ FilterChainConstant.APP_KEY+"参数的值没有找到对应的应用或appSecret为空"
                    ,response);
            return;
        }


        TreeMap<String,String> stringTreeMap = new TreeMap<>();
        for (String key : bodyJson.keySet()) {
            Object object = bodyJson.get(key);
            if (null != object) {
                if ((object instanceof CharSequence) || (object instanceof Number)) {
                    stringTreeMap.put(key,object.toString());
                } else {
                    if (object instanceof JSONObject) {
                        JSONObject jsonObject0 = (JSONObject)object;
                        stringTreeMap.put(key, JSONObject.toJSONString(jsonObject0, SerializerFeature.MapSortField));
                    } else if (object instanceof JSONArray) {
                        JSONArray jsonArray0 = (JSONArray)object;
                        stringTreeMap.put(key, JSONArray.toJSONString(jsonArray0, SerializerFeature.MapSortField));
                    } else {
                        stringTreeMap.put(key, JSON.toJSONString(object));
                    }
                }
            } else {
                stringTreeMap.put(key, null);
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(RequestInfoHolder.getUri());
        stringBuilder.append(StringConstant.QUESTION_MARK);
        stringTreeMap.forEach((x,y) -> {
            if (!FilterChainConstant.SIGN.equals(x)) {
                stringBuilder.append(x);
                stringBuilder.append(StringConstant.EQUAL);
                stringBuilder.append(y);
                stringBuilder.append(StringConstant.AND);
            }
        });
        stringBuilder.append(FilterChainConstant.APP_SECRET);
        stringBuilder.append(StringConstant.EQUAL);
        stringBuilder.append(appSecret);

        String needMd5String = stringBuilder.toString();
        logger.debug("需要被md5散列的字符串是:"+needMd5String);

        String md5 = Md5Util.getMD5(needMd5String);
        if (!sign.equals(md5)) {
            logger.debug("正确的md5值应该是:" + md5);
            doSthBeforeReturn("签名不匹配，请仔细阅读文档", response);
            return;
        }


        /**
         * 如果参数是放到body里面的，则还要把参数返回body中
         */
        /**
         * 移除业务不需要的参数
         */
        bodyJson.remove(FilterChainConstant.SIGN);
        bodyJson.remove(FilterChainConstant.TIMESTAMP);
        bodyJson.remove(FilterChainConstant.NONCE);

        PutStringToBodyWrapper putStringToBodyWrapper =
                new PutStringToBodyWrapper(request);
        putStringToBodyWrapper.setBody(bodyJson.toJSONString());
        filterChain.doFilter(putStringToBodyWrapper,response);
    }


    /**
     *
     * @param httpServletRequest
     * @return
     */
    private boolean needCheckApiSign(HttpServletRequest httpServletRequest) {

        String uri = httpServletRequest.getRequestURI();

        /**
         * 如果请求来自doc，则跳过校验签名
         */
        if (EnvUtil.isDevOrTest(environment) &&
                DocAccessTokenConstant.DOC_ACCESS_TOKEN_HEADER_DEFAULT_VALUE.equals(
                        httpServletRequest.getHeader(DocAccessTokenConstant.DOC_ACCESS_TOKEN_HEADER_NAME))) {
            logger.info("当前请求来自于doc，不校验签名");
            return false;
        }
        for (String x : filterChainProperties.getNoSignPatterns()) {
            if (antPathMatcher.match(x,uri)) {
                logger.info("当前uri跳过校验签名");
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param bodyObject
     * @param keyName
     * @return
     */
    private static String getValueFromBody(JSONObject bodyObject, String keyName) {

        Object object = bodyObject.get(keyName);
        if (null != object) {
            String keyValue = object.toString();
            if (StringUtils.isNotBlank(keyValue)) {
                return keyValue;
            }
        }
        return null;
    }

    /**
     *
     * @param message
     * @param httpServletResponse
     */
    private static void doSthBeforeReturn(String message,HttpServletResponse httpServletResponse) {
        singletonInvalidRequest();
        INVALID_REQUEST.setMessage(message);
        ServletResponseUtil.write(httpServletResponse,INVALID_REQUEST);
    }

    public static void main(String[] args) {
        //System.out.println("//111/2////444/4/555".replaceAll("/+","/"));

        //AntPathMatcher antPathMatcher = new AntPathMatcher();
        //System.out.println(antPathMatcher.match("/**/swagger-ui.html/**","/ump/swagger-ui.html"));

        JSONObject jsonObject0 = new JSONObject();
        jsonObject0.put("b","b");
        jsonObject0.put("c","c");
        jsonObject0.put("a","a");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("b","b");
        jsonObject.put("c","c");
        jsonObject.put("a",jsonObject0);

        System.out.println(JSONObject.toJSONString(jsonObject, SerializerFeature.MapSortField));
    }
}
