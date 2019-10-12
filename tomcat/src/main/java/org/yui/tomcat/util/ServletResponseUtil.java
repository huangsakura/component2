package org.yui.tomcat.util;

import org.yui.base.bean.api.JsonResult;
import org.yui.base.util.JsonUtil;
import lombok.extern.log4j.Log4j2;
import org.yui.base.bean.constant.StringConstant;

import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author huangjinlong
 */
@Log4j2
public abstract class ServletResponseUtil {

    /**
     * 默认的content type
     */
    public static final String DEFAULT_CONTENT_TYPE = "text/html;charset=UTF-8";

    /**
     * 往response里面写内容
     * @param servletResponse
     * @param jsonResult
     */
    public static void write(ServletResponse servletResponse, JsonResult jsonResult) {

        String text = JsonUtil.toJsonStringQuietly(jsonResult);
        write(servletResponse,text);
    }


    /**
     *
     * @param servletResponse
     * @param text
     */
    public static void write(ServletResponse servletResponse, String text) {
        servletResponse.setContentType(DEFAULT_CONTENT_TYPE);
        servletResponse.setCharacterEncoding(StringConstant.UTF_8);
        try {
            servletResponse.getWriter().write(text);
        } catch (IOException e) {
            log.error("ServletResponse获取writer失败:{}",e.getMessage());
        }
    }
}
