package com.jskim.banking.policy.fee;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component("rateFeePolicy")
public class RateFeePolicy implements FeePolicy {

  private static final BigDecimal RATE = new BigDecimal("0.01");
  private static final RoundingMode roundingMode = RoundingMode.HALF_EVEN;
  private static final int scale = 0;

  @Override
  public BigDecimal calculateFee(BigDecimal amount) {
    return amount.multiply(RATE).setScale(scale, roundingMode);
  }
}
