package com.jskim.banking.service;

import com.jskim.banking.annotation.RedisLock;
import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.repository.account.AccountRepository;
import com.jskim.banking.exception.ResourceNotFoundException;
import static com.jskim.banking.exception.ResourceNotFoundException.ResourceNotFoundExceptionCode.ACCOUNT_NOT_FOUND;
import static com.jskim.banking.policy.Constant.ACCOUNT_LOCK_PREFIX;
import com.jskim.banking.request.command.DepositCommand;
import com.jskim.banking.response.AccountDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DepositService {

  private final AccountRepository accountRepository;

  public DepositService(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @RedisLock(keys = {"#dto.accountNo"}, prefix = ACCOUNT_LOCK_PREFIX)
  @Transactional
  public AccountDTO deposit(DepositCommand dto) {
    Account account = accountRepository.findByAccountNoAndDeletedFalse(dto.getAccountNo())
        .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND));
    account.deposit(dto.getAmount());
    return AccountDTO.valueOf(account);
  }
}
