package com.jskim.banking.policy.dailylimit;

import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.entity.DailyAccountUsage;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component("withdrawalDailyLimitPolicy")
public class WithdrawalDailyLimitPolicy implements DailyLimitPolicy {

  @Override
  public boolean validateDailyLimit(Account account, DailyAccountUsage dailyUsage, BigDecimal amount) {
    return BigDecimal.ZERO.compareTo(
        account.getDailyWithdrawalLimit().subtract(dailyUsage.getDailyWithdrawalUsed())
            .subtract(amount)) <= 0;
  }

  @Override
  public void addDailyUsed(DailyAccountUsage dailyUsage, BigDecimal amount) {
    dailyUsage.addDailyWithdrawalUsed(amount);
  }
}
