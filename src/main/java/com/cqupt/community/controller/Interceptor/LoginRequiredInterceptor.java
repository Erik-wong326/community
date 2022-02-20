package com.cqupt.community.controller.Interceptor;

import com.cqupt.community.annotation.LoginRequired;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 检查登录状态的拦截器
 * 防止在非登录状态下,能够访问登陆状态下的页面
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/20 16:19
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    /**
     *
     * @param request
     * @param response
     * @param handler 拦截目标
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // HandlerMethod -> 判断是不是拦截到一个方法
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class); //从方法中取注解
            //判断注解/用户是否为空
            if (loginRequired != null && hostHolder.getUser() == null) {
                //非登录状态,则重定向到登录状态
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }
        return true;
    }

}
