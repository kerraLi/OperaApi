package com.ywxt.Handler;

import com.auth0.jwt.JWT;
import com.ywxt.Domain.LogOperation;
import com.ywxt.Service.LogOperationService;
import net.sf.json.JSONArray;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Aspect
@Service
public class OperationLogAopHandler {

    @Resource
    private LogOperationService logOperationService;

    // 定义控制器切点
    @Pointcut("execution(public * com.ywxt.Controller..*.*(..)) && !@annotation(com.ywxt.Annotation.NotOperationAction)")
    public void pointCut() {
    }


    // 配置连接点 方法开始执行时通知
    @Around("pointCut()")
    public Object beforeLog(ProceedingJoinPoint pjp) throws Throwable, Exception {
        // 获取当前执行controller方法:pjp || 通过注解定义切点：直接过滤
        // Signature signature = pjp.getSignature();
        // MethodSignature methodSignature = (MethodSignature) signature;
        // Method targetMethod = methodSignature.getMethod();
        // if (!targetMethod.isAnnotationPresent(OperationAction.class)) {
        //     return pjp.proceed();
        // }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authToken = request.getHeader("Authorization");
        int userId = Integer.parseInt(JWT.decode(authToken).getSubject());
        LogOperation logOperation = new LogOperation();
        logOperation.setUserId(userId);
        logOperation.setSessionId(request.getSession().getId());
        logOperation.setPath(request.getPathInfo());
        logOperation.setInParam(JSONArray.fromObject(request.getParameterMap()).toString());
        logOperation.setStatus("unfinished");
        logOperation.setTime(new Date());
        logOperation.setIp(this.getIpAddress(request));
        logOperationService.create(logOperation);
        return pjp.proceed();
    }

    // 方法执行完后通知
    @After("pointCut()")
    public void afterLog() {
    }

    // 执行成功后通知
    @AfterReturning(returning = "rvt", pointcut = "pointCut()")
    public void afterReturningLog(Object rvt) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String sessionId = request.getSession().getId();
        LogOperation logOperation = logOperationService.getLogOperation(sessionId);
        logOperation.setStatus("normal");
        logOperation.setOutParam(JSONArray.fromObject(rvt).toString());
        logOperationService.update(logOperation);
    }

    // 抛出异常后通知
    @AfterThrowing(throwing = "e", pointcut = "pointCut()")
    public void afterThrowingLog(Throwable e) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String sessionId = request.getSession().getId();
        LogOperation logOperation = logOperationService.getLogOperation(sessionId);
        logOperation.setStatus("error");
        logOperation.setOutParam("[" + e.getClass() + ":=:" + e.getMessage() + "]");
        logOperationService.update(logOperation);
    }

    // 获取真实ip
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
