package com.jskim.banking.service;

import com.jskim.banking.data.entity.Account;
import com.jskim.banking.data.repository.account.AccountRepository;
import com.jskim.banking.data.repository.transaction.AccountTransactionRepository;
import com.jskim.banking.exception.AccountException;
import static com.jskim.banking.exception.AccountException.AccountExceptionCode.BALANCE_EXISTS;
import static com.jskim.banking.exception.AccountException.AccountExceptionCode.OPEN_FAILED;
import com.jskim.banking.exception.ResourceNotFoundException;
import static com.jskim.banking.exception.ResourceNotFoundException.ResourceNotFoundExceptionCode.ACCOUNT_NOT_FOUND;
import static com.jskim.banking.policy.Constant.ACCOUNT_CREATION_MAX_RETRY;
import static com.jskim.banking.policy.Constant.ACCOUNT_LENGTH;
import static com.jskim.banking.policy.Constant.INITIAL_DAILY_TRANSFER_LIMIT;
import static com.jskim.banking.policy.Constant.INITIAL_DAILY_WITHDRAWAL_LIMIT;
import com.jskim.banking.request.command.CreateAccountCommand;
import com.jskim.banking.request.command.DeleteAccountCommand;
import com.jskim.banking.request.query.AccountTransactionQuery;
import com.jskim.banking.response.AccountDTO;
import com.jskim.banking.response.AccountTransactionDTO;
import com.jskim.banking.util.random.RandomUtil;
import com.jskim.banking.util.response.CursorPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountService {

  private final AccountRepository accountRepository;
  private final AccountTransactionRepository accountTransactionRepository;

  public AccountService(AccountRepository accountRepository,
      AccountTransactionRepository accountTransactionRepository) {
    this.accountRepository = accountRepository;
    this.accountTransactionRepository = accountTransactionRepository;
  }

  @Transactional
  public AccountDTO create(CreateAccountCommand dto) {
    for (int attempt = 0; attempt < ACCOUNT_CREATION_MAX_RETRY; attempt++) {
      try {
        String accountNo = RandomUtil.generateRandomDigit(ACCOUNT_LENGTH);
        Account account = new Account(accountNo, dto.getOwner(),
            INITIAL_DAILY_WITHDRAWAL_LIMIT, INITIAL_DAILY_TRANSFER_LIMIT);
        accountRepository.save(account);
        return AccountDTO.valueOf(account);
      } catch (DataIntegrityViolationException e) {
        log.debug("account create exception : [{}]", e.getMessage(), e);
      }
    }
    throw new AccountException(OPEN_FAILED);
  }

  @Transactional
  public void delete(DeleteAccountCommand dto) {
    Account account = accountRepository.findByAccountNoAndDeletedFalse(dto.getAccountNo())
        .orElseThrow(() -> new ResourceNotFoundException(ACCOUNT_NOT_FOUND));

    if (account.existBalance()) {
      throw new AccountException(BALANCE_EXISTS);
    }

    accountRepository.delete(account);
  }

  public CursorPage<AccountTransactionDTO, Long> getTransactions(String accountNo,
      AccountTransactionQuery query) {
    return accountTransactionRepository.findAllByAccountNo(accountNo, query);
  }
}
