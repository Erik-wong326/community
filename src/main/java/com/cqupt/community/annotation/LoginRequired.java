package com.cqupt.community.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Target(ElementType.METHOD) 用于注解方法
 * @Retention(RetentionPolicy.RUNTIME) 注解在运行时生效
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/20 16:16
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
