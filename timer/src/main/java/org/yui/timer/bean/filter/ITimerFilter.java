package org.yui.timer.bean.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yui.base.bean.api.JsonResult;
import org.yui.timer.config.properties.TimerProperties;
import org.yui.tomcat.util.ServletResponseUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huangjinlong
 * @time 2019-03-26 11:09
 * @description
 */
public final class ITimerFilter extends OncePerRequestFilter {

    private TimerProperties timerProperties;

    public ITimerFilter(TimerProperties timerProperties) {
        this.timerProperties = timerProperties;
    }

    private static final String KEY = "key";

    private static JsonResult JSON_RESULT_NO_KEY = null;
    static {
        JSON_RESULT_NO_KEY = new JsonResult();
        JSON_RESULT_NO_KEY.setCode("REQUEST_PARAMETER_IS_NECESSARY");
        JSON_RESULT_NO_KEY.setMessage("参数"+KEY+"必传");
    }

    private static JsonResult JSON_RESULT_ERROR_KEY = null;
    static {
        JSON_RESULT_ERROR_KEY = new JsonResult();
        JSON_RESULT_ERROR_KEY.setCode("PARAMETER_ERROR");
        JSON_RESULT_ERROR_KEY.setMessage("参数"+KEY+"的值错误");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {

        String key = httpServletRequest.getParameter(KEY);
        if (StringUtils.isBlank(key)) {
            ServletResponseUtil.write(httpServletResponse,JSON_RESULT_NO_KEY);
            return;
        }
        if (!timerProperties.getInvokeKey().equals(key)) {
            ServletResponseUtil.write(httpServletResponse,JSON_RESULT_ERROR_KEY);
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
