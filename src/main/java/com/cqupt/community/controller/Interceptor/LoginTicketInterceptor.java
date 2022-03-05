package com.cqupt.community.controller.Interceptor;

import com.cqupt.community.entity.LoginTicket;
import com.cqupt.community.entity.User;
import com.cqupt.community.service.UserService;
import com.cqupt.community.util.CookieUtil;
import com.cqupt.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 从 request 中获取cookie
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/19 18:07
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从request 中获取 cookie, 再从cookie中获取凭证
        // 因此 封装了这一操作在 CookieUtil 中
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            // 1.查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 2.检查凭证是否有效
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证(ticket) 查询用户(user)
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                // 因为浏览器访问服务器是多对一的，因此要考虑多线程，并发的情况
                // 此时要考虑线程的隔离 User 存到-> ThreadLocal
                // 考虑到其他地方也可能有这种场景 , 因此封装为 HostHolder 工具类
                hostHolder.setUser(user);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
