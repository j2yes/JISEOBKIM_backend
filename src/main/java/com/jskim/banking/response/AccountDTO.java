package com.jskim.banking.response;

import com.jskim.banking.data.entity.Account;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDTO {

  private String accountNo;
  private String owner;
  private BigDecimal balance;

  public static AccountDTO valueOf(Account account) {
    AccountDTO dto = new AccountDTO();
    dto.setAccountNo(account.getAccountNo());
    dto.setOwner(account.getOwner());
    dto.setBalance(account.getBalance());
    return dto;
  }
}
