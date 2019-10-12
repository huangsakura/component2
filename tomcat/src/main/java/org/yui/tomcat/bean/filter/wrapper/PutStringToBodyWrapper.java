package org.yui.tomcat.bean.filter.wrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author huangjinlong
 * @time 2019-07-27 10:49
 * @description
 */
public class PutStringToBodyWrapper extends HttpServletRequestWrapper {

    private byte[] body = null;

    public PutStringToBodyWrapper(HttpServletRequest request) {
        super(request);
    }

    public void setBody(@NotNull String json) {
        this.body = json.getBytes();
    }

    public void setBody(@NotNull byte[] body) {
        this.body = body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
