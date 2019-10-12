package org.yui.tomcat.util;

import org.hibernate.validator.constraints.NotBlank;
import org.yui.base.bean.constant.StringConstant;

import javax.annotation.Nullable;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * @author huangjinlong
 * 需要提取到公共工程当中去
 */
public abstract class CookieUtil {

    /**
     *
     * @param request
     * @param cookieName
     * @return
     */
    @Nullable
    public static String getCookieValue(@NotNull HttpServletRequest request, @NotBlank String cookieName) {
        return Optional.ofNullable(getCookie(request,cookieName))
                .map(Cookie::getValue).orElse(null);
    }

    /**
     *
     * @param request
     * @param cookieName
     * @return
     */
    @Nullable
    public static Cookie getCookie(@NotNull HttpServletRequest request, @NotBlank String cookieName) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     *
     * @param response
     * @param cookieName
     * @param value
     */
    public static void writeCookie(@NotNull HttpServletResponse response, @NotBlank String cookieName, String value) {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setPath(StringConstant.SLASH);
        cookie.setMaxAge(3600);
        response.addCookie(cookie);
    }
}
