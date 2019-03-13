package com.ywxt.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 免登录接口标记
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {

    // 标记接口是否跳过登陆
    boolean login() default false;

    // 标记接口是否跳过鉴权
    boolean permission() default true;
}