package com.jskim.banking.policy.fee;

import java.math.BigDecimal;

public interface FeePolicy {

  BigDecimal calculateFee(BigDecimal amount);
}
