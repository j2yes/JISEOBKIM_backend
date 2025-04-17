package com.jskim.banking.service;

import com.jskim.banking.annotation.RedisLock;
import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.entity.DailyAccountUsage;
import com.jskim.banking.data.repository.account.AccountRepository;
import com.jskim.banking.data.repository.dailylimit.DailyAccountUsageRepository;
import com.jskim.banking.exception.ResourceNotFoundException;
import static com.jskim.banking.exception.ResourceNotFoundException.ResourceNotFoundExceptionCode.ACCOUNT_NOT_FOUND;
import static com.jskim.banking.policy.Constant.ACCOUNT_LOCK_PREFIX;
import com.jskim.banking.policy.dailylimit.DailyLimitPolicy;
import com.jskim.banking.request.command.WithdrawCommand;
import com.jskim.banking.response.AccountDTO;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WithdrawalService {

  private final AccountRepository accountRepository;
  private final DailyAccountUsageRepository dailyAccountUsageRepository;

  private final DailyLimitPolicy withdrawalDailyLimitPolicy;

  public WithdrawalService(AccountRepository accountRepository,
      DailyAccountUsageRepository dailyAccountUsageRepository,
      @Qualifier("withdrawalDailyLimitPolicy") DailyLimitPolicy withdrawalDailyLimitPolicy) {
    this.accountRepository = accountRepository;
    this.dailyAccountUsageRepository = dailyAccountUsageRepository;
    this.withdrawalDailyLimitPolicy = withdrawalDailyLimitPolicy;
  }

  @RedisLock(keys = {"#dto.accountNo"}, prefix = ACCOUNT_LOCK_PREFIX)
  @Transactional
  public AccountDTO withdraw(WithdrawCommand dto) {
    Account account = accountRepository.findByAccountNoAndDeletedFalse(dto.getAccountNo())
        .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND));

    LocalDate today = LocalDate.now();
    DailyAccountUsage dailyUsage = dailyAccountUsageRepository.findByAccountAndDate(account, today)
        .orElse(new DailyAccountUsage(account, today));
    dailyAccountUsageRepository.save(dailyUsage);

    account.withdraw(dto.getAmount(), dailyUsage, withdrawalDailyLimitPolicy);

    return AccountDTO.valueOf(account);
  }
}
