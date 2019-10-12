package org.yui.filterchain.bean.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.yui.base.bean.api.JsonResult;
import org.yui.base.exception.BusinessException;
import org.yui.base.util.JsonUtil;
import org.yui.filterchain.bean.constant.FilterChainConstant;
import org.yui.tomcat.bean.constant.TomcatConstant;
import org.yui.tomcat.bean.filter.wrapper.PutStringToBodyWrapper;
import org.yui.tomcat.util.ServletRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yui.tomcat.util.ServletResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author huangjinlong
 * @time 2019-09-30 09:21
 * @description
 */
public class PrintParameterFilter extends OncePerRequestFilter {

    private RedissonClient redissonClient;

    public PrintParameterFilter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            String contentType = request.getContentType();
            if (StringUtils.isBlank(contentType) || !contentType.contains(TomcatConstant.CONTENT_TYPE_APPLICATION_JSON)) {
                throw new BusinessException(JsonResult.FAIL,
                        "content-type属性必须包含" + TomcatConstant.CONTENT_TYPE_APPLICATION_JSON,false);
            }
            /**
             * 遍历header
             */
            Map<String,String> headerMap = new HashMap<>(32);
            Enumeration<String> enumeration = request.getHeaderNames();
            while (enumeration.hasMoreElements()) {
                String name = enumeration.nextElement();
                String value = request.getHeader(name);
                headerMap.put(name,value);
            }
            logger.debug("header参数:" + JsonUtil.toJsonString(headerMap));

            /**
             * 遍历body
             */
            PutStringToBodyWrapper putStringToBodyWrapper = new PutStringToBodyWrapper(request);
            String body = ServletRequestUtil.getStringFromBody(request);
            logger.info("body入参:" + body);

            try {
                JSONObject jsonObject = JSON.parseObject(body);
                RequestInfoHolder.setAppKey(Optional.ofNullable(jsonObject.get(FilterChainConstant.APP_KEY))
                        .map(Object::toString).orElse(null));
            } catch (Exception e) {
                logger.error("body中的参数不能转为json对象:" + e.getMessage());
            }

            if (StringUtils.isBlank(RequestInfoHolder.getAppKey())) {
                throw new BusinessException(JsonResult.FAIL, "从body信息中无法解析出appKey信息",false);
            }

            RMap<String,Object> rMap = redissonClient.getMap(FilterChainConstant.ABUTMENT_APP_SECRET_REDIS_KEY);
            String appSecret = Optional.ofNullable(rMap.get(RequestInfoHolder.getAppKey()))
                    .map(Object::toString).orElse(null);
            if (StringUtils.isBlank(appSecret)) {
                throw new BusinessException(JsonResult.FAIL, "根据appKey无法找到密钥，请联系网站管理员",false);
            }
            RequestInfoHolder.setAppSecret(appSecret);

            putStringToBodyWrapper.setBody(body);
            filterChain.doFilter(putStringToBodyWrapper,response);
        } catch (BusinessException e) {
            JsonResult jsonResult = new JsonResult();
            jsonResult.setCode(e.getCode());
            jsonResult.setMessage(e.getMessage());
            ServletResponseUtil.write(response,jsonResult);
        }
    }
}
