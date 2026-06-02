package com.project.ecommerce.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Logging Aspect - Demonstrates AOP for cross-cutting logging concerns
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("execution(* com.project.ecommerce.service..*(..))")
    public void serviceMethods(){}

    @Pointcut("execution(* com.project.ecommerce.controller..*(..))")
    public void controllerMethods(){}

    /**
     * Before advice - executes before method execution
     */
    @Before("serviceMethods() || controllerMethods()")
    public void logBefore(JoinPoint joinPoint){
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("[BEFORE] {}.{} called with arguments: {}", className, methodName, Arrays.toString(args));
    }

    /**
     * AfterReturning advice - executes after successful method execution
     */
    @AfterReturning(pointcut = "serviceMethods() || controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result){
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.info("[AFTER RETURNING] {}.{} returned: {}", className, methodName, result);
    }

    /**
     * AfterThrowing advice - executes when method throws exception
     */
    @AfterThrowing(pointcut = "serviceMethods() || controllerMethods()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception){
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.error("[AFTER THROWING] {}.{} threw exception: {}", className, methodName, exception.getMessage());
    }

    /**
     * After advice - executes after method execution (regardless of outcome)
     */
    @After("serviceMethods() || controllerMethods()")
    public void logAfter(JoinPoint joinPoint){
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        logger.info("[AFTER] {}.{} execution completed", className, methodName);
    }

}
