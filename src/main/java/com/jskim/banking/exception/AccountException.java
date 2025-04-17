package com.jskim.banking.exception;


import com.jskim.banking.util.response.ResponseCode;

public class AccountException extends BaseException {

  public enum AccountExceptionCode implements ResponseCode {
    BALANCE_NOT_ENOUGH("ACC-001", "balance not enough"),
    BALANCE_EXISTS("ACC-002", "account balance exists"),
    OPEN_FAILED("ACC-003", "account open failed");

    private final String code;
    private final String message;

    AccountExceptionCode(String code, String message) {
      this.code = code;
      this.message = message;
    }

    @Override
    public String getCode() {
      return code;
    }

    @Override
    public String getMessage() {
      return message;
    }
  }

  public AccountException(AccountExceptionCode code) {
    super(code);
  }
}
