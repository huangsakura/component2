package org.yui.filterchain.bean.filter;

import org.yui.base.util.UrlUtil;
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

/**
 * @author huangjinlong
 * @time 2019-07-27 11:12
 * @description
 */
public class UrlDecodeBodyFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String contentType = request.getContentType();
        if (StringUtils.isNotBlank(contentType)
                && contentType.contains(TomcatConstant.CONTENT_TYPE_APPLICATION_JSON)) {
            PutStringToBodyWrapper putStringToBodyWrapper =
                    new PutStringToBodyWrapper(request);

            String body = ServletRequestUtil.getStringFromBody(request);
            String newBody = UrlUtil.decode(body);
            putStringToBodyWrapper.setBody(newBody);
            filterChain.doFilter(putStringToBodyWrapper,response);
        } else {
            filterChain.doFilter(request,response);
        }
    }
}
