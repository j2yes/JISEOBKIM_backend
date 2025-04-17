package com.jskim.banking.data;

import static com.jskim.banking.data.code.AccountTransactionType.DEPOSIT;
import static com.jskim.banking.data.code.AccountTransactionType.WITHDRAWAL;
import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.entity.DailyAccountUsage;
import com.jskim.banking.exception.AccountException;
import static com.jskim.banking.exception.AccountException.AccountExceptionCode.BALANCE_NOT_ENOUGH;
import com.jskim.banking.exception.DailyLimitException;
import static com.jskim.banking.exception.DailyLimitException.DailyLimitExceptionCode.LIMIT_EXCEED;
import static com.jskim.banking.policy.Constant.INITIAL_DAILY_TRANSFER_LIMIT;
import static com.jskim.banking.policy.Constant.INITIAL_DAILY_WITHDRAWAL_LIMIT;
import com.jskim.banking.policy.dailylimit.DailyLimitPolicy;
import com.jskim.banking.policy.dailylimit.WithdrawalDailyLimitPolicy;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountTest {

  private Account account;

  @BeforeEach
  void setUp() {
    account = new Account("1234567890123", "owner",
        INITIAL_DAILY_WITHDRAWAL_LIMIT, INITIAL_DAILY_TRANSFER_LIMIT);
  }

  @Test
  void 입금_정상_테스트() {
    BigDecimal depositAmount = new BigDecimal("500000");

    account.deposit(depositAmount);

    assertThat(account.getBalance()).isEqualByComparingTo(depositAmount);
    assertThat(account.getAccountTransactions())
        .hasSize(1)
        .extracting("accountTransactionType")
        .containsExactly(DEPOSIT);
  }

  @Test
  void 출금_정상_테스트() {
    BigDecimal depositAmount = new BigDecimal("1000000");
    BigDecimal withdrawAmount = new BigDecimal("200000");

    account.deposit(depositAmount);

    DailyLimitPolicy policy = new WithdrawalDailyLimitPolicy();
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());

    account.withdraw(withdrawAmount, dailyUsage, policy);

    assertThat(account.getBalance()).isEqualByComparingTo(depositAmount.subtract(withdrawAmount));
    assertThat(account.getAccountTransactions())
        .hasSize(2)
        .extracting("accountTransactionType")
        .contains(DEPOSIT, WITHDRAWAL);
  }

  @Test
  void 출금_일_한도_초과_테스트() {
    BigDecimal amount = INITIAL_DAILY_WITHDRAWAL_LIMIT.add(BigDecimal.ONE);

    account.deposit(amount);

    DailyLimitPolicy policy = new WithdrawalDailyLimitPolicy();
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());

    assertThatThrownBy(() -> account.withdraw(amount, dailyUsage, policy))
        .isInstanceOf(DailyLimitException.class)
        .hasMessageContaining(LIMIT_EXCEED.getMessage());
  }

  @Test
  void 출금_잔액_부족_테스트() {
    BigDecimal depositAmount = new BigDecimal("100000");
    BigDecimal withdrawAmount = depositAmount.add(BigDecimal.ONE);

    account.deposit(depositAmount);

    DailyLimitPolicy policy = new WithdrawalDailyLimitPolicy();
    DailyAccountUsage dailyUsage = new DailyAccountUsage(account, LocalDate.now());

    assertThatThrownBy(() -> account.withdraw(withdrawAmount, dailyUsage, policy))
        .isInstanceOf(AccountException.class)
        .hasMessageContaining(BALANCE_NOT_ENOUGH.getMessage());
  }

  @Test
  void 잔액_있음_테스트() {
    account.deposit(BigDecimal.ONE);
    assertThat(account.existBalance()).isTrue();
  }

  @Test
  void 잔액_없음_테스트() {
    assertThat(account.existBalance()).isFalse();
  }
}
