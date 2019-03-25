package com.ywxt.Config;


import com.ywxt.Handler.AllowCrossDomainHandler;
import com.ywxt.Handler.AuthenticationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;

    public InterceptorConfig() {
        super();
    }

    @Bean
    public AllowCrossDomainHandler AllowCrossDomainHandler() {
        return new AllowCrossDomainHandler();
    }

    @Bean
    public AuthenticationHandler AuthenticationHandler() {
        return new AuthenticationHandler();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(AllowCrossDomainHandler()).addPathPatterns("/**");
        registry.addInterceptor(AuthenticationHandler()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}