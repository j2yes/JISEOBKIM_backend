package com.jskim.banking.policy.fee;

import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component("noFeePolicy")
public class NoFeePolicy implements FeePolicy {

  @Override
  public BigDecimal calculateFee(BigDecimal amount) {
    return BigDecimal.ZERO;
  }
}
