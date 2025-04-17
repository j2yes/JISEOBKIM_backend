package com.jskim.banking.util.response;

public enum DefaultResponseCode implements ResponseCode {
  SUCCESS("CMN-001", "success"),
  FAIL("CMN-002", "fail");

  private final String code;
  private final String message;

  DefaultResponseCode(String code, String message) {
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
