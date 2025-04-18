package net.uoay.chat.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class UncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(UncaughtExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        logger.warn("Exception message {}", ex.getMessage());
        logger.warn("Method name {}", method.getName());
        for (var param: params) {
            logger.warn("Param {}", param.toString());
        }
    }
}
