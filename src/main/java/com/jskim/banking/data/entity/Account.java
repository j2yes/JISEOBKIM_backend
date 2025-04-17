package com.jskim.banking.data.entity;

import static com.jskim.banking.data.code.AccountTransactionType.DEPOSIT;
import static com.jskim.banking.data.code.AccountTransactionType.WITHDRAWAL;
import com.jskim.banking.exception.AccountException;
import static com.jskim.banking.exception.AccountException.AccountExceptionCode.BALANCE_NOT_ENOUGH;
import com.jskim.banking.exception.DailyLimitException;
import static com.jskim.banking.exception.DailyLimitException.DailyLimitExceptionCode.LIMIT_EXCEED;
import com.jskim.banking.policy.dailylimit.DailyLimitPolicy;
import static jakarta.persistence.CascadeType.ALL;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(
    name = "account"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id = ? and version= ?")
public class Account {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String accountNo;
  @Column(nullable = false)
  private String owner;

  @Column(nullable = false, precision = 23, scale = 4)
  @Digits(integer = 19, fraction = 4)
  private BigDecimal balance;
  @Column(nullable = false, precision = 23, scale = 4)
  @Digits(integer = 19, fraction = 4)
  private BigDecimal dailyWithdrawalLimit;
  @Column(nullable = false, precision = 23, scale = 4)
  @Digits(integer = 19, fraction = 4)
  private BigDecimal dailyTransferLimit;

  private boolean deleted;

  @OneToMany(cascade = ALL, mappedBy = "account")
  private List<AccountTransaction> accountTransactions = new ArrayList<>();

  @Version
  private int version;

  public Account(String accountNo, String owner, BigDecimal initialDailyWithdrawalLimit,
      BigDecimal initialDailyTransferLimit) {
    this.accountNo = accountNo;
    this.owner = owner;
    this.balance = BigDecimal.ZERO;
    this.dailyWithdrawalLimit = initialDailyWithdrawalLimit;
    this.dailyTransferLimit = initialDailyTransferLimit;
  }

  public void deposit(BigDecimal amount) {
    this.balance = this.balance.add(amount);

    accountTransactions.add(new AccountTransaction(amount, DEPOSIT, this));
  }

  public void withdraw(BigDecimal amount,
      DailyAccountUsage dailyUsage, DailyLimitPolicy policy) {

    if (!policy.validateDailyLimit(this, dailyUsage, amount)) {
      throw new DailyLimitException(LIMIT_EXCEED);
    }

    policy.addDailyUsed(dailyUsage, amount);

    if (this.balance.compareTo(amount) < 0) {
      throw new AccountException(BALANCE_NOT_ENOUGH);
    }

    this.balance = this.balance.subtract(amount);

    accountTransactions.add(new AccountTransaction(amount, WITHDRAWAL, this));
  }

  public boolean existBalance() {
    return balance.compareTo(BigDecimal.ZERO) > 0;
  }
}
