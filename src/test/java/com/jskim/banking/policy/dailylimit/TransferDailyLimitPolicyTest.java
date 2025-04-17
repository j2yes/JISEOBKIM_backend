package com.jskim.banking.policy.dailylimit;

import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.entity.DailyAccountUsage;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TransferDailyLimitPolicyTest {

  private TransferDailyLimitPolicy policy;
  private Account account;

  @BeforeEach
  void setUp() {
    account = new Account("0123456789012", "owner",
        new BigDecimal("3000"), new BigDecimal("5000"));
    policy = new TransferDailyLimitPolicy();
  }

  @ParameterizedTest
  @ValueSource(strings = {"1000", "2000", "3000", "5000"})
  void 이체_한도_이내_유효성_검사_테스트(String inputAmount) {
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());
    BigDecimal amountToTransfer = new BigDecimal(inputAmount);

    boolean result = policy.validateDailyLimit(account, dailyUsage, amountToTransfer);

    assertThat(result).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"6000", "7000"})
  void 이체_한도_초과_유효성_검사_테스트(String inputAmount) {
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());
    BigDecimal amountToTransfer = new BigDecimal(inputAmount);

    boolean result = policy.validateDailyLimit(account, dailyUsage, amountToTransfer);

    assertThat(result).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"3400", "8503"})
  void 이체_한도_추가_누적_테스트(String inputAmount) {
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());
    BigDecimal initialUsed = dailyUsage.getDailyTransferUsed();
    BigDecimal amountToTransfer = new BigDecimal(inputAmount);

    policy.addDailyUsed(dailyUsage, amountToTransfer);

    assertThat(dailyUsage.getDailyTransferUsed()).isEqualByComparingTo(
        initialUsed.add(amountToTransfer));
  }
}
