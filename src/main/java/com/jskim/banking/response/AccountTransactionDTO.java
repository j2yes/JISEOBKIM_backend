package com.jskim.banking.response;

import com.jskim.banking.data.code.AccountTransactionType;
import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransactionDTO {

  private Long id;
  private BigDecimal amount;
  private AccountTransactionType accountTransactionType;
  protected LocalDateTime transactionDateTime;

  @QueryProjection
  public AccountTransactionDTO(Long id, BigDecimal amount, AccountTransactionType accountTransactionType,
      LocalDateTime transactionDateTime) {
    this.id = id;
    this.amount = amount;
    this.accountTransactionType = accountTransactionType;
    this.transactionDateTime = transactionDateTime;
  }
}
