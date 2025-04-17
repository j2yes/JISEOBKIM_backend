package com.jskim.banking.util.response;

import com.jskim.banking.exception.BaseException;
import lombok.Getter;

@Getter
public class Response<T> {

  private final T data;
  private final String code;
  private final String message;

  public Response(BaseException e) {
    this(null, e.getResponseCode());
  }

  public Response(ResponseCode responseCode) {
    this(null, responseCode);
  }

  private Response(T data) {
    this(data, DefaultResponseCode.SUCCESS);
  }

  public Response(T data, ResponseCode responseCode) {
    this.data = data;
    this.code = responseCode.getCode();
    this.message = responseCode.getMessage();
  }

  public static <T> Response<T> success(T data) {
    return new Response<>(data);
  }

  public static <T> Response<T> success() {
    return new Response<>(DefaultResponseCode.SUCCESS);
  }

  public static <T> Response<T> fail(BaseException e) {
    return new Response<>(e);
  }

  public static <T> Response<T> fail() {
    return new Response<>(DefaultResponseCode.FAIL);
  }
}

