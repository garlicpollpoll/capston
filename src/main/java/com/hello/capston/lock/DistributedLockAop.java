package com.hello.capston.lock;

import com.hello.capston.lock.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class DistributedLockAop {

    private final AopForTransaction aopForTransaction;
    private final RedissonClient redissonClient;

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    @Around("@annotation(com.hello.capston.lock.annotation.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        RLock rLock = redissonClient.getLock(key);

        boolean available = false;
        try {
            available = rLock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());
            if (!available) {
                return false;
            }

            return aopForTransaction.proceed(joinPoint);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread was interrupted while acquiring lock");
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already Unlock {} {}", method.getName(), key);
            }
        }
    }
}
