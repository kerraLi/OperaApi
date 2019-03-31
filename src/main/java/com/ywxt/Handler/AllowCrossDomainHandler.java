package com.ywxt.Handler;

import com.ywxt.Utils.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// 处理跨域
@Component
public class AllowCrossDomainHandler implements HandlerInterceptor {

    @Autowired
    private Environment env;
    @Value("${allow.cross.url}")
    private String domains;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String origin = request.getHeader("Origin");
        Set<String> allowOrigins = new HashSet<String>(Arrays.asList(domains.split(",")));
        if ((env.getProperty("spring.profiles.active") != null && !env.getProperty("spring.profiles.active").equals("prod"))
                || allowOrigins.contains(origin)) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT, PATCH");
            response.setHeader("Access-Control-Max-Age", "0");
            response.setHeader("Access-Control-Allow-Headers", "authorization, Authorization, Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("XDomainRequestAllowed", "1");
        }
        return true;
    }

}
