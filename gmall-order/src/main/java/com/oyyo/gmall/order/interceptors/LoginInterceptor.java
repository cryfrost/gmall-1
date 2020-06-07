package com.oyyo.gmall.order.interceptors;

import com.oyyo.core.bean.UserInfo;
import com.oyyo.core.utils.CookieUtils;
import com.oyyo.core.utils.JwtUtils;
import com.oyyo.gmall.order.config.JwtProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private JwtProperties jwtProperties;

    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal();

    /**
     * 统一获取登录状态
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        UserInfo userInfo = new UserInfo();
        //获取cookie中的 token 及 user-key
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());

        //判断是否有 token
        if (StringUtils.isNotBlank(token)) {
            //解析token
            Map<String, Object> infoFromToken = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            userInfo.setUserId(Long.valueOf(infoFromToken.get("id").toString()));
        }

        THREAD_LOCAL.set(userInfo);

        return super.preHandle(request, response, handler);
    }

    /**
     * 删除线程
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        THREAD_LOCAL.remove();
    }

    /**
     * 获取userinfo
     * @return
     */
    public static UserInfo getThreadLocalUserInfo(){
        return THREAD_LOCAL.get();
    }


}
