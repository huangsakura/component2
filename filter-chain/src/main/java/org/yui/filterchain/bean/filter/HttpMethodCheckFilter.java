package org.yui.filterchain.bean.filter;

import org.yui.base.bean.api.JsonResult;
import org.yui.tomcat.util.ServletResponseUtil;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * @author huangjinlong
 * @time 2019-04-08 13:20
 * @description
 */
public class HttpMethodCheckFilter extends OncePerRequestFilter {

    private static volatile JsonResult JSON_RESULT = null;

    /**
     * 单例模式
     */
    private static void singleton() {
        if (null == JSON_RESULT) {
            synchronized (HttpMethodCheckFilter.class) {
                if (null == JSON_RESULT) {
                    JSON_RESULT = new JsonResult();
                    JSON_RESULT.setMessage("post请求的隐藏域"+HiddenHttpMethodFilter.DEFAULT_METHOD_PARAM
                            +"的值必须是put或delete");
                    JSON_RESULT.setCode("PARAMETER_ERROR");
                }
            }
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String[] httpMethod = {request.getMethod().toUpperCase()};
        boolean[] hasError = {false};

        if (RequestMethod.POST.name().equals(httpMethod[0])) {
            Optional<String> stringOptional = Optional.ofNullable(
                    request.getParameter(HiddenHttpMethodFilter.DEFAULT_METHOD_PARAM));
            stringOptional.ifPresent((method) -> {
                if (!RequestMethod.PUT.name().equalsIgnoreCase(method)
                        && !RequestMethod.DELETE.name().equalsIgnoreCase(method)) {
                    hasError[0] = true;
                }
            });
        }
        if (hasError[0]) {
            singleton();
            ServletResponseUtil.write(response,JSON_RESULT);
            return;
        }

        filterChain.doFilter(request,response);
    }
}
