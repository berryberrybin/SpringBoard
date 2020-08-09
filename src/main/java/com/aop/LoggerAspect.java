package com.aop;

import org.springframework.stereotype.Component;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Aspect
public class LoggerAspect {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("execution(* com..controller.*Controller.*(..)) or " +
            "execution(* com..service.*Impl.*(..)) or" +
            " execution(* com..mapper.*Mapper.*(..))")
    public Object logPrint(ProceedingJoinPoint joinPoint) throws Throwable {
        String type = "";
        String name = joinPoint.getSignature().getDeclaringTypeName();
        if (name.indexOf("Controller") > -1) {
            type = "Controller  \t:  ";
        }
        else if (name.indexOf("Service") > -1) {
            type = "ServiceImpl  \t:  ";
        }
        else if (name.indexOf("Mapper") > -1) {
            type = "Mapper  \t\t:  ";
        }
        log.info(type + name + "." + joinPoint.getSignature().getName() + "()");
        return joinPoint.proceed();
    }
}

