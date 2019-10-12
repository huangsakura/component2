package org.yui.tomcat.util;

import org.yui.base.exception.BusinessException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author huangjinlong
 * @time 2019-07-27 10:44
 * @description
 */
@Log4j2
public abstract class ServletRequestUtil {
    /**
     *
     * @param request
     * @return
     */
    @NotNull
    public static String getStringFromBody(HttpServletRequest request) {

        try (BufferedReader bufferedReader = request.getReader()) {
            return IOUtils.toString(bufferedReader);
        } catch (IOException e) {
            log.error("IO错误:{}",e.getMessage());
            throw new BusinessException("IO_EXCEPTION","IO错误",false);
        }
    }

    public static void main(String[] args) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\系统文件夹\\桌面\\2019-08-04-1.log\\2019-08-04-1.log"));
        System.out.println(IOUtils.toString(bufferedReader).length());
    }
}
