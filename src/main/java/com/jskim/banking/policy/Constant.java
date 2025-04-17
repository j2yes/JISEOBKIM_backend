package com.jskim.banking.policy;

import java.math.BigDecimal;

public class Constant {

  public static final BigDecimal INITIAL_DAILY_WITHDRAWAL_LIMIT = BigDecimal.valueOf(1000000);
  public static final BigDecimal INITIAL_DAILY_TRANSFER_LIMIT = BigDecimal.valueOf(3000000);
  public static final int ACCOUNT_LENGTH = 13;
  public static final int ACCOUNT_CREATION_MAX_RETRY = 3;
  public static final String ACCOUNT_LOCK_PREFIX = "account-lock:";
}
