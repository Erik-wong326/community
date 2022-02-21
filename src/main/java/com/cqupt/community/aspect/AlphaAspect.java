package com.cqupt.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * AOP - demo测试
 * @author Erik_Wong
 * @version 1.0
 * @date 2022/2/21 18:17
 */
//@Component
//@Aspect
public class AlphaAspect {
    @Pointcut("execution(* com.cqupt.community.service.*.*(..))")
    public void pointcut() {
    }

    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    /**
     * 返回值以后织入代码
     */
    @AfterReturning("pointcut()")
    public void afterRetuning() {
        System.out.println("afterRetuning");
    }

    /**
     * 抛异常以后织入代码
     */
    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    /**
     * 前后都织入
     * @param joinPoint 目标织入连接点
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object obj = joinPoint.proceed(); //调用目标组件(target)方法
        System.out.println("around after");
        return obj;
    }
}
