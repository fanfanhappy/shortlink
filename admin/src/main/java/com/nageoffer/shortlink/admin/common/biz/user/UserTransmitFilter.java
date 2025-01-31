package com.nageoffer.shortlink.admin.common.biz.user;

import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 用户信息传输过滤器
 *
 */
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {


    private final StringRedisTemplate stringRedisTemplate;

    private static final List<String> IGNORE_URI = Lists.newArrayList(
            "/api/short-link/admin/v1/user/login",
            "/api/short-link/admin/v1/user/has-username"
    );


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        if(!IGNORE_URI.contains(requestURI))
        {
            String method = httpServletRequest.getMethod();
            if(!(Objects.equals(requestURI , "/api/short-link/admin/v1/user") && Objects.equals(method , "POST")))
            {
                //从请求头里拿到username
                String userName = httpServletRequest.getHeader("username");
                //从请求头拿到token
                String token = httpServletRequest.getHeader("token");
                //从redis里拿到存入的用户（JSON）
                Object userJson = stringRedisTemplate.opsForHash().get("login_" + userName, token);
                if (userJson != null) {
                    //将json转为对象
                    UserInfoDTO userInfoDTO = JSON.parseObject(userJson.toString(), UserInfoDTO.class);
                    //加入用户上下文
                    UserContext.setUser(userInfoDTO);
                }
            }

        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}
