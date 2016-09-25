package com.taotao.cart.handlerInterceptor;

import javax.servlet.http.HttpServletRequest;

//拦截器,使用拦截器功能实现用户是否登录的验证 

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.cart.bean.User;
import com.taotao.cart.service.UserService;
import com.taotao.cart.threadlocal.UserThreadLocal;
import com.taotao.common.utils.CookieUtils;

//使用springmvc中拦截器验证用户是否登录

public class UserLoginHandlerInterceptor implements HandlerInterceptor {

    public static final String COOKIE_NAME = "TT_TOKEN";


    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        
        UserThreadLocal.set(null);//清空当前线程中的User对象
        //登录页面sso.taotao.com/user/login.html
        String token = CookieUtils.getCookieValue(request, COOKIE_NAME);
        if (StringUtils.isEmpty(token)) {
            // 未登录状态,重定向到登录页面
//            response.sendRedirect(loginUrl);
            return true;//放行
        }
        //从redis中查询token对应的值
        User user = this.userService.queryUserByToken(token);
        if (null == user) {
            // 未登录状态
            return true;//放行
        }
        //处于登录状态
        
        UserThreadLocal.set(user); //将User对象放置到ThreadLocal中
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {

    }

}
