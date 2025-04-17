package com.jskim.banking.policy.fee;

import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RateFeePolicyTest {

  private RateFeePolicy feePolicy;

  @BeforeEach
  void setUp() {
    feePolicy = new RateFeePolicy();
  }

  @Test
  void 수수료_정상_계산_테스트() {
    BigDecimal amount = new BigDecimal("10000");

    BigDecimal fee = feePolicy.calculateFee(amount);

    assertThat(fee).isEqualByComparingTo(new BigDecimal("100"));
  }

  @Test
  void 수수료_반올림_내림_계산_테스트() {
    BigDecimal amount = new BigDecimal("3333");

    BigDecimal fee = feePolicy.calculateFee(amount);

    assertThat(fee).isEqualByComparingTo(new BigDecimal("33"));
  }

  @Test
  void 수수료_반올림_올림_계산_테스트() {
    BigDecimal amount = new BigDecimal("3389");

    BigDecimal fee = feePolicy.calculateFee(amount);

    assertThat(fee).isEqualByComparingTo(new BigDecimal("34"));
  }
}
