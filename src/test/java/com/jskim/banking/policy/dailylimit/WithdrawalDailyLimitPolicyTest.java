package com.jskim.banking.policy.dailylimit;

import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.entity.DailyAccountUsage;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class WithdrawalDailyLimitPolicyTest {

  private WithdrawalDailyLimitPolicy policy;
  private Account account;

  @BeforeEach
  void setUp() {
    account = new Account("0123456789012", "owner",
        new BigDecimal("3000"), new BigDecimal("5000"));
    policy = new WithdrawalDailyLimitPolicy();
  }

  @ParameterizedTest
  @ValueSource(strings = {"1000", "2000", "3000"})
  void 출금_한도_이내_유효성_검사_테스트(String inputAmount) {
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());
    BigDecimal amountToWithdraw = new BigDecimal(inputAmount);

    boolean result = policy.validateDailyLimit(account, dailyUsage, amountToWithdraw);

    assertThat(result).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"4000", "7000"})
  void 출금_한도_초과_유효성_검사_테스트(String inputAmount) {
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());
    BigDecimal amountToWithdraw = new BigDecimal(inputAmount);

    boolean result = policy.validateDailyLimit(account, dailyUsage, amountToWithdraw);

    assertThat(result).isFalse();
  }

  @ParameterizedTest
  @ValueSource(strings = {"5500", "9742"})
  void 출금_한도_추가_누적_테스트(String inputAmount) {
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());
    BigDecimal initialUsed = dailyUsage.getDailyWithdrawalUsed();
    BigDecimal amountToWithdraw = new BigDecimal(inputAmount);

    policy.addDailyUsed(dailyUsage, amountToWithdraw);

    assertThat(dailyUsage.getDailyWithdrawalUsed()).isEqualByComparingTo(
        initialUsed.add(amountToWithdraw));
  }
}
