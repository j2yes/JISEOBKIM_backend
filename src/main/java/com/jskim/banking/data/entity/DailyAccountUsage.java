package com.jskim.banking.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "daily_account_usage",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"account_id", "date"})}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class DailyAccountUsage {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, updatable = false)
  protected LocalDate date;

  @Column(nullable = false, precision = 23, scale = 4)
  @Digits(integer = 19, fraction = 4)
  private BigDecimal dailyWithdrawalUsed;
  @Column(nullable = false, precision = 23, scale = 4)
  @Digits(integer = 19, fraction = 4)
  private BigDecimal dailyTransferUsed;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
  private Account account;

  @Version
  private int version;

  public DailyAccountUsage(Account account, LocalDate date) {
    this.dailyWithdrawalUsed = BigDecimal.ZERO;
    this.dailyTransferUsed = BigDecimal.ZERO;
    this.account = account;
    this.date = date;
  }

  public void addDailyWithdrawalUsed(BigDecimal amount) {
    this.dailyWithdrawalUsed = this.dailyWithdrawalUsed.add(amount);
  }

  public void addDailyTransferUsed(BigDecimal amount) {
    this.dailyTransferUsed = this.dailyTransferUsed.add(amount);
  }
}
