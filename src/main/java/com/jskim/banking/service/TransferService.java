package com.jskim.banking.service;

import com.jskim.banking.annotation.RedisLock;
import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.entity.DailyAccountUsage;
import com.jskim.banking.data.entity.Transfer;
import com.jskim.banking.data.repository.account.AccountRepository;
import com.jskim.banking.data.repository.dailylimit.DailyAccountUsageRepository;
import com.jskim.banking.data.repository.transfer.TransferRepository;
import com.jskim.banking.exception.ResourceNotFoundException;
import static com.jskim.banking.exception.ResourceNotFoundException.ResourceNotFoundExceptionCode.ACCOUNT_NOT_FOUND;
import static com.jskim.banking.policy.Constant.ACCOUNT_LOCK_PREFIX;
import com.jskim.banking.policy.dailylimit.DailyLimitPolicy;
import com.jskim.banking.policy.fee.RateFeePolicy;
import com.jskim.banking.request.command.TransferCommand;
import com.jskim.banking.response.AccountDTO;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransferService {

  private final AccountRepository accountRepository;
  private final DailyAccountUsageRepository dailyAccountUsageRepository;
  private final TransferRepository transferRepository;

  private final DailyLimitPolicy transferDailyLimitPolicy;
  private final RateFeePolicy rateFeePolicy;

  public TransferService(AccountRepository accountRepository,
      DailyAccountUsageRepository dailyAccountUsageRepository,
      TransferRepository transferRepository,
      @Qualifier("transferDailyLimitPolicy") DailyLimitPolicy transferDailyLimitPolicy,
      @Qualifier("rateFeePolicy") RateFeePolicy rateFeePolicy) {
    this.accountRepository = accountRepository;
    this.dailyAccountUsageRepository = dailyAccountUsageRepository;
    this.transferRepository = transferRepository;
    this.transferDailyLimitPolicy = transferDailyLimitPolicy;
    this.rateFeePolicy = rateFeePolicy;
  }

  @RedisLock(keys = {"#dto.fromAccountNo", "#dto.toAccountNo"}, prefix = ACCOUNT_LOCK_PREFIX)
  @Transactional
  public AccountDTO transfer(TransferCommand dto) {

    Account fromAccount = accountRepository.findByAccountNoAndDeletedFalse(dto.getFromAccountNo())
        .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND));
    Account toAccount = accountRepository.findByAccountNoAndDeletedFalse(dto.getToAccountNo())
        .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND));

    BigDecimal fee = rateFeePolicy.calculateFee(dto.getAmount());

    LocalDate today = LocalDate.now();
    DailyAccountUsage dailyUsage = dailyAccountUsageRepository.findByAccountAndDate(fromAccount,
            today)
        .orElse(new DailyAccountUsage(fromAccount, today));
    dailyAccountUsageRepository.save(dailyUsage);

    fromAccount.withdraw(dto.getAmount().add(fee), dailyUsage, transferDailyLimitPolicy);
    toAccount.deposit(dto.getAmount());

    Transfer transfer = new Transfer(dto.getAmount(), fee, fromAccount, toAccount);
    transferRepository.save(transfer);

    return AccountDTO.valueOf(fromAccount);
  }
}
