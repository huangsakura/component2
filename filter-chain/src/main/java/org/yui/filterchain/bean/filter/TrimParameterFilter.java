package org.yui.filterchain.bean.filter;

import org.yui.filterchain.bean.filter.wrapper.TrimHttpServletRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huangjinlong
 * @time 2019-04-12 14:58
 * @description
 */
public class TrimParameterFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /**
         * 将所以的字符串参数值进行trim操作
         */
        filterChain.doFilter(new TrimHttpServletRequestWrapper(request), response);
    }
}
