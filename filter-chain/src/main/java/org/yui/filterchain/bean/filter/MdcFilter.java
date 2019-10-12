package org.yui.filterchain.bean.filter;

import org.yui.base.bean.constant.LogConstant;
import org.yui.base.util.RandomUtil;
import org.yui.tomcat.util.IpUtil;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huangjinlong
 * @time 2019-09-26 18:07
 * @description
 */
public class MdcFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            /**
             * 引入这个mdc，是为了在日志里面，同一个请求能看完这个请求整套的日志输出，
             * 方便用 grep
             */
            MDC.put(LogConstant.MDC_REQUEST_ID_NAME, RandomUtil.did());

            RequestInfoHolder.init();
            RequestInfoHolder.setIp(IpUtil.getIp(request));
            RequestInfoHolder.setUri(request.getRequestURI().replaceAll("/+","/"));
            RequestInfoHolder.setMethod(request.getMethod());
            RequestInfoHolder.setRequest(request);

            logger.info("请求开始");
            logger.info("uri:" + RequestInfoHolder.getMethod() + " " + RequestInfoHolder.getUri());
            logger.info("ip:" + RequestInfoHolder.getIp());

            filterChain.doFilter(request,response);
        } finally {
            logger.info("请求结束");
            MDC.clear();
            RequestInfoHolder.remove();
        }
    }
}
