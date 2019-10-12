package org.yui.filterchain.bean.filter;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author huangjinlong
 * @time 2019-10-09 14:31
 * @description
 */
public abstract class RequestInfoHolder {

    static final ThreadLocal<Info> THREAD_LOCAL = new ThreadLocal<>();

    @Setter
    @Getter
    private static class Info {

        private String ip;

        private HttpServletRequest request;

        /**
         * post
         * get
         * 这种
         */
        private String method;

        private String uri;

        private String appKey;

        private String appSecret;
    }

    /**
     * 每个线程，只允许在脚手架里面被调用一次
     */
    static void init() {
        if (null == THREAD_LOCAL.get()) {
            THREAD_LOCAL.set(new Info());
        }
    }

    /**
     * 每个线程，只允许在脚手架里面被调用一次
     * @param ip
     */
    static void setIp(String ip) {
        Validate.notBlank(ip);
        Info info = THREAD_LOCAL.get();
        info.setIp(ip);
        THREAD_LOCAL.set(info);
    }

    /**
     *
     * @param appKey
     */
    static void setAppKey(String appKey) {
        Validate.notBlank(appKey);
        Info info = THREAD_LOCAL.get();
        info.setAppKey(appKey);
        THREAD_LOCAL.set(info);
    }

    /**
     *
     * @param appSecret
     */
    static void setAppSecret(String appSecret) {
        Validate.notBlank(appSecret);
        Info info = THREAD_LOCAL.get();
        info.setAppSecret(appSecret);
        THREAD_LOCAL.set(info);
    }

    /**
     *每个线程，只允许在脚手架里面被调用一次
     * @param request
     */
    static void setRequest(HttpServletRequest request) {
        Validate.notNull(request);
        Info info = THREAD_LOCAL.get();
        info.setRequest(request);
        THREAD_LOCAL.set(info);
    }

    /**
     *每个线程，只允许在脚手架里面被调用一次
     * @param method
     */
    static void setMethod(String method) {
        Validate.notBlank(method);
        Info info = THREAD_LOCAL.get();
        info.setMethod(method);
        THREAD_LOCAL.set(info);
    }

    /**
     *每个线程，只允许在脚手架里面被调用一次
     * @param uri
     */
    static void setUri(String uri) {
        Validate.notBlank(uri);
        Info info = THREAD_LOCAL.get();
        info.setUri(uri);
        THREAD_LOCAL.set(info);
    }

    /**
     *
     * @return
     */
    @Nullable
    public static String getIp() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(Info::getIp).orElse(null);
    }

    /**
     *
     * @return
     */
    @Nullable
    public static String getMethod() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(Info::getMethod).orElse(null);
    }

    /**
     *
     * @return
     */
    public static String getAppKey() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(Info::getAppKey).orElse(null);
    }

    /**
     *
     * @return
     */
    public static String getAppSecret() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(Info::getAppSecret).orElse(null);
    }

    /**
     *
     * @return
     */
    @Nullable
    public static String getUri() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(Info::getUri).orElse(null);
    }

    /**
     *
     * @return
     */
    @Nullable
    public static HttpServletRequest getRequest() {
        return Optional.ofNullable(THREAD_LOCAL.get())
                .map(Info::getRequest).orElse(null);
    }

    /**
     *
     */
    public static void remove() {
        THREAD_LOCAL.remove();
    }
}
