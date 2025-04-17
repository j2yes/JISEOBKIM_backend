package com.jskim.banking.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RedisLock {

  /**
   * 락 키 목록 (SpEL 지원, 예: {"#fromAccountId", "#toAccountId"})
   */
  String[] keys();

  String prefix() default "lock:";

  long waitTime() default 3L;

  long leaseTime() default 10L;

  TimeUnit timeUnit() default TimeUnit.SECONDS;
}
