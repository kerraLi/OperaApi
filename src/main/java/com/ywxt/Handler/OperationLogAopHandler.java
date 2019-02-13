package com.ywxt.Handler;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Service
public class OperationLogAopHandler {

    // 定义控制器切点
    @Pointcut("execution(public * com.ywxt.Controller..*.*(..))")
    public void pointCut() {
    }


    // 配置连接点 方法开始执行时通知
    @Before("pointCut()")
    public void beforeLog() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String id = request.getSession().getId();
            String a = request.getPathInfo();
            System.out.println(a);
        } catch (NullPointerException e) {
            // 非http请求不做处理
        }
        System.out.println("===============================开始执行前置通知  日志记录:beforeLog");
    }

    // 方法执行完后通知
    @After("pointCut()")
    public void afterLog() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String id = request.getSession().getId();
            request.getPathInfo();
        } catch (NullPointerException e) {
            // 非http请求不做处理
        }
        System.out.println("===============================开始执行前置通知  日志记录:beforeLog开始执行后置通知 日志记录:afterLog");
    }

    // 执行成功后通知
    @AfterReturning("pointCut()")
    public void afterReturningLog() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String id = request.getSession().getId();
            request.getPathInfo();
        } catch (NullPointerException e) {
            // 非http请求不做处理
        }
        System.out.println("===============================开始执行前置通知  日志记录:beforeLog方法成功执行后通知 日志记录:afterReturningLog");
    }

    // 抛出异常后通知
    @AfterThrowing("pointCut()")
    public void afterThrowingLog() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String id = request.getSession().getId();
            request.getPathInfo();
        } catch (NullPointerException e) {
            // 非http请求不做处理
        }
        System.out.println("===============================开始执行前置通知  日志记录:beforeLog方法抛出异常后执行通知 日志记录:afterThrowingLog");
    }

}
