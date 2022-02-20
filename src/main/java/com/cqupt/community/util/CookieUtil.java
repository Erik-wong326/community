package com.cqupt.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/19 18:10
 */
public class CookieUtil {
    public static String getValue(HttpServletRequest request, String name) {
        //1.空值判断
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空!");
        }

        Cookie[] cookies = request.getCookies();
        //2.遍历 Cookie 取值
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        //3.cookie 都遍历完了,没有找到需要的数据
        return null;
    }
}
