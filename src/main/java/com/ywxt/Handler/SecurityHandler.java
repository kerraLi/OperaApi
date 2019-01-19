package com.ywxt.Handler;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//创建SpringSecurity配置类
public class SecurityHandler extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
       web.ignoring().mvcMatchers("/**");

}
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").access("hasRole('ADMIN')");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

    }
}
