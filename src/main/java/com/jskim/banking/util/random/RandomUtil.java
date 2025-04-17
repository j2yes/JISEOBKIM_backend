package com.jskim.banking.util.random;

import java.security.SecureRandom;

public class RandomUtil {

  private static final SecureRandom random = new SecureRandom();

  public static String generateRandomDigit(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(random.nextInt(10));
    }
    return sb.toString();
  }
}
