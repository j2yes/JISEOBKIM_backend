package com.jskim.banking.exception;


import com.jskim.banking.util.response.ResponseCode;

public class DailyLimitException extends BaseException {

  public enum DailyLimitExceptionCode implements ResponseCode {
    LIMIT_EXCEED("DAL-001", "daily limit exceeded");

    private final String code;
    private final String message;

    DailyLimitExceptionCode(String code, String message) {
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

  public DailyLimitException(DailyLimitExceptionCode code) {
    super(code);
  }
}
