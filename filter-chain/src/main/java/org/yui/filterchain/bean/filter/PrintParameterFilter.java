package org.yui.filterchain.bean.filter;

import org.yui.base.util.JsonUtil;
import org.yui.base.util.MapUtil;
import org.yui.tomcat.bean.constant.TomcatConstant;
import org.yui.tomcat.bean.filter.wrapper.PutStringToBodyWrapper;
import org.yui.tomcat.util.ServletRequestUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huangjinlong
 * @time 2019-09-30 09:21
 * @description
 */
public class PrintParameterFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

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
         * 遍历parameter或body
         */
        HttpServletRequest newHttpServletRequest = request;
        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType) && contentType.contains(TomcatConstant.CONTENT_TYPE_APPLICATION_JSON)) {

            PutStringToBodyWrapper putStringToBodyWrapper = new PutStringToBodyWrapper(request);

            String body = ServletRequestUtil.getStringFromBody(request);
            logger.info("body入参:" + body);
            putStringToBodyWrapper.setBody(body);
            newHttpServletRequest = putStringToBodyWrapper;
        } else {
            Map<String, String> map = MapUtil.getStringMap(request.getParameterMap());
            logger.info("parameter入参:" + JsonUtil.toJsonStringQuietly(map));
        }
        filterChain.doFilter(newHttpServletRequest,response);
    }
}
