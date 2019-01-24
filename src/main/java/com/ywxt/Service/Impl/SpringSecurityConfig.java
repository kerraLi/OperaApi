package com.ywxt.Service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/***
 *
 * @Author:
 * @Description:
 * @date:
 *
 ****/
@Configuration
@Component
@EnableWebSecurity
@ComponentScan("com.ywxt.Service.Impl.*")
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /****
     * 1）公开链接
     * 2）私有链接访问策略
     * 3）授权认证
     *
     *
     * 三个全部重写configure即可
     */

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private BCryptPasswordEncoder encoder;
   // 装载BCrypt密码编码器
   /*@Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();*/
    /*****
     * 1)配置忽略安全控制的访问地址(公开链接)
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/img/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/plugins/**");
        web.ignoring().antMatchers("/*.html");
        //用户注册的地址
        web.ignoring().antMatchers("/login");
    }

    /****
     * 2)配置安全链接的访问策略(非公开链接)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {


        //禁用CSRF安全策略
        http.csrf().disable()          // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
               .authorizeRequests().antMatchers("/**").access("hasRole('USER')")
            .antMatchers("/admin/**").access("hasRole('ADMIN')")
                .anyRequest().authenticated();
        //开启登录配置
        http.formLogin()
                .loginProcessingUrl("/login")       //指定登录处理地址
                .loginPage("/login.html")      //登录页面
                //.defaultSuccessUrl("/admin/index.html",true) //登录成功后跳转的页面,true:登录成功后总是跳转到该页面
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        //获取Writer对象
                        PrintWriter writer = response.getWriter();
                        //输出的错误信息
                        writer.write("账号或者密码不正确");
                        //关闭资源
                        writer.flush();
                        writer.close();
                    }
                }).successHandler(new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                //获取Writer对象
                PrintWriter writer = response.getWriter();
                //输出的错误信息
                writer.write("success");
                //关闭资源
                writer.flush();
                writer.close();
            }
        });

        //开启登出配置
        http.logout()
                .logoutSuccessUrl("/login.html")//登出后跳转的页面
                .logoutUrl("/logout")               //登出处理路径
                .invalidateHttpSession(true);       //让session无效

        //一个账号只允许在一处登录
        http.sessionManagement()
                .maximumSessions(1)                 //一个账号同时登录的地方只允许有一个
                .expiredUrl("/login.html");   //如果该账号在其他机器上登录，则该用户需要跳转到登录页面重新登录




        //禁用iframe相关属性
        http.headers().frameOptions().disable();
    }


    /****
     * 3)授权认证(创建账号，授予角色)
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //自定义授权认证类
       auth.userDetailsService(userDetailsService)      //指定自定义认证类
               .passwordEncoder(encoder);                //指定加密对象

    }
}
