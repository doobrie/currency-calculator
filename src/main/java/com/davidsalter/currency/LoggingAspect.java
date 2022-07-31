/**
 * Command Line Current Converter
 *
 * Author: David Salter 2022
 */
package com.davidsalter.currency;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Simple Aspect to log entry into Service Methods.
 * The implementation is fairly naive and only logs out that a method has being invoked.
 * If required, this could be extended to log exceptions being thrown, arguments passed into
 * methods and return values from methods.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.stereotype.Service *)")
    private void currencyServiceMethods() {}

    @Before("currencyServiceMethods()")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.info( "Entering method {}", joinPoint.getSignature());
    }
}
