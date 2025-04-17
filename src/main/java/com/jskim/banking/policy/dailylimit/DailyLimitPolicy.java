package com.jskim.banking.policy.dailylimit;

import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.entity.DailyAccountUsage;
import java.math.BigDecimal;

public interface DailyLimitPolicy {

  boolean validateDailyLimit(Account account, DailyAccountUsage dailyUsage, BigDecimal amount);
  void addDailyUsed(DailyAccountUsage dailyUsage, BigDecimal amount);
}
