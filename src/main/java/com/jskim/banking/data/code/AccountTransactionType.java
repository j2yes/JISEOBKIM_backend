package com.jskim.banking.data.code;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum AccountTransactionType {

  WITHDRAWAL("01"),
  DEPOSIT("02");

  private final String dbCode;

  AccountTransactionType(String dbCode) {
    this.dbCode = dbCode;
  }

  public static AccountTransactionType of(String dbCode) {
    return Arrays.stream(values())
        .filter(v -> dbCode.equals(v.dbCode))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("%s is invalid code", dbCode)));
  }
}
