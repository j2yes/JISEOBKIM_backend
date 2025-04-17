package com.jskim.banking.exception;

import com.jskim.banking.util.response.ResponseCode;
import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

  private final ResponseCode responseCode;

  public BaseException(ResponseCode responseCode) {
    super(responseCode.getMessage());
    this.responseCode = responseCode;
  }
}
