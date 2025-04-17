package com.jskim.banking.data.entity;

import com.jskim.banking.data.code.AccountTransactionType;
import com.jskim.banking.data.converter.AccountTransactionTypeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import static jakarta.persistence.GenerationType.IDENTITY;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Digits;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(
    name = "account_transaction"
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class AccountTransaction {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(nullable = false, precision = 23, scale = 4)
  @Digits(integer = 19, fraction = 4)
  private BigDecimal amount;
  @Column(name = "account_event_type", nullable = false)
  @Convert(converter = AccountTransactionTypeConverter.class)
  private AccountTransactionType accountTransactionType;
  @CreationTimestamp
  @Column(updatable = false)
  protected LocalDateTime transactionDateTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false, updatable = false)
  private Account account;

  public AccountTransaction(BigDecimal amount, AccountTransactionType accountTransactionType,
      Account account) {
    this.amount = amount;
    this.accountTransactionType = accountTransactionType;
    this.account = account;
  }
}
