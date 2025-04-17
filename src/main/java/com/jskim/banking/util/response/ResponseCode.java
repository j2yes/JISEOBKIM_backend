package com.jskim.banking.util.response;

import java.io.Serializable;

public interface ResponseCode extends Serializable {

  String getCode();

  String getMessage();
}