package com.jskim.banking.aop;

import com.jskim.banking.annotation.RedisLock;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Aspect
@Order(1)
@Component
@RequiredArgsConstructor
@Slf4j
public class RedisLockAspect {

  private final RedissonClient redissonClient;

  @Around("@annotation(redisLock)")
  public Object around(ProceedingJoinPoint joinPoint, RedisLock redisLock) throws Throwable {
    List<String> keyList = resolveKeys(joinPoint, redisLock);
    List<String> sortedKeys = keyList.stream().sorted().toList();
    List<RLock> locks = new ArrayList<>();

    boolean allLocked = false;
    long waitTime = redisLock.waitTime();
    long leaseTime = redisLock.leaseTime();
    TimeUnit unit = redisLock.timeUnit();

    try {
      // 순차적으로 락 획득
      for (String key : sortedKeys) {
        RLock lock = redissonClient.getLock(key);
        if (lock.tryLock(waitTime, leaseTime, unit)) {
          locks.add(lock);
        } else {
          throw new IllegalStateException("lock is held by other trx : " + key);
        }
      }

      allLocked = true;
      return joinPoint.proceed();

    } finally {
      // 역순으로 락 해제
      if (allLocked) {
        Collections.reverse(locks);
        for (RLock lock : locks) {
          if (lock.isHeldByCurrentThread()) {
            lock.unlock();
          }
        }
      }
    }
  }

  private List<String> resolveKeys(JoinPoint joinPoint, RedisLock redisLock) {
    EvaluationContext context = new StandardEvaluationContext();

    Object[] args = joinPoint.getArgs();
    String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();

    // 메서드 파라미터 이름과 값을 context 에 등록
    if (paramNames != null) {
      for (int i = 0; i < paramNames.length; i++) {
        context.setVariable(paramNames[i], args[i]);
      }
    }

    ExpressionParser parser = new SpelExpressionParser();
    String prefix = redisLock.prefix();

    List<String> keyList = new ArrayList<>();

    for (String spel : redisLock.keys()) {
      try {
        String value = parser.parseExpression(spel).getValue(context, String.class);
        if (value == null) {
          throw new IllegalArgumentException("Invalid RedisLock Key : " + spel);
        }

        keyList.add(prefix + value);

      } catch (Exception e) {
        throw new IllegalArgumentException("Invalid RedisLock Key : " + spel, e);
      }
    }

    return keyList;
  }
}
