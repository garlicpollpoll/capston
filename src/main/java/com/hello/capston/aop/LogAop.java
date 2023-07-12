package com.hello.capston.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
@Slf4j
public class LogAop {

    @Around("execution(* com.hello.capston..*(..))")
    public void doTrace(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        log.info("[log trace] {} args = {}", joinPoint.getSignature(), args);
    }
}
