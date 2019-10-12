package org.yui.filterchain.bean.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.Validate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yui.safety.aes.AesUtil;
import org.yui.tomcat.bean.filter.wrapper.PutStringToBodyWrapper;
import org.yui.tomcat.util.ServletRequestUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author huangjinlong
 * @time 2019-10-12 13:43
 * @description
 */
public class DecryptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        PutStringToBodyWrapper putStringToBodyWrapper = new PutStringToBodyWrapper(httpServletRequest);
        String body = ServletRequestUtil.getStringFromBody(httpServletRequest);

        JSONObject jsonObject = JSON.parseObject(body);
        boolean decrypted = decrypt(jsonObject);
        String newBody = body;
        if (decrypted) {
            newBody = jsonObject.toJSONString();
            logger.info("解密后的body入参为:" + newBody);
        }
        putStringToBodyWrapper.setBody(newBody);
        filterChain.doFilter(putStringToBodyWrapper,httpServletResponse);
    }

    /**
     *
     * @param jsonObject
     */
    private boolean decrypt(@NotNull JSONObject jsonObject) {
        Validate.notBlank(RequestInfoHolder.getAppSecret());

        boolean result = false;
        for (String key : jsonObject.keySet()) {
            if (key.startsWith("password")) {
                result = true;
                Object object = jsonObject.get(key);
                if (null != object) {
                    if (object instanceof CharSequence) {
                        byte[] bytes = AesUtil.decrypt(object.toString().getBytes(),
                                RequestInfoHolder.getAppSecret().getBytes(),AesUtil.ALGORITHM0);
                        if (bytes == null) {
                            logger.error(key + "属性解密失败");
                            jsonObject.put(key,null);
                        } else {
                            jsonObject.put(key,new String(bytes));
                        }
                    } else {
                        logger.warn(key + "属性的值的类型不是String");
                    }
                } else {
                    jsonObject.put(key,null);
                }
            }
        }
        return result;
    }
}
