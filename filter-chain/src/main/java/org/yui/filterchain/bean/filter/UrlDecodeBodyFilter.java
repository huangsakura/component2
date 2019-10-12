package org.yui.filterchain.bean.filter;

import org.yui.base.util.UrlUtil;
import org.yui.tomcat.bean.filter.wrapper.PutStringToBodyWrapper;
import org.yui.tomcat.util.ServletRequestUtil;
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

        PutStringToBodyWrapper putStringToBodyWrapper = new PutStringToBodyWrapper(request);
        String body = ServletRequestUtil.getStringFromBody(request);
        String newBody = UrlUtil.decode(body);
        putStringToBodyWrapper.setBody(newBody);
        filterChain.doFilter(putStringToBodyWrapper,response);
    }
}
