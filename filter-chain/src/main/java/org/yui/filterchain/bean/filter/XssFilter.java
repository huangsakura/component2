package org.yui.filterchain.bean.filter;

import org.yui.filterchain.config.DefenceUtil;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yui.tomcat.bean.filter.wrapper.PutStringToBodyWrapper;
import org.yui.tomcat.util.ServletRequestUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huangjinlong
 * 过滤所有请求
 *
 * 过滤xss攻击
 */
public final class XssFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        PutStringToBodyWrapper putStringToBodyWrapper = new PutStringToBodyWrapper(httpServletRequest);
        String body = ServletRequestUtil.getStringFromBody(httpServletRequest);
        String newBody = DefenceUtil.cleanXss(body);
        putStringToBodyWrapper.setBody(newBody);
        filterChain.doFilter(putStringToBodyWrapper,httpServletResponse);
    }
}
