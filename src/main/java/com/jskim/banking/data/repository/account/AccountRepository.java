package com.jskim.banking.data.repository.account;

import com.jskim.banking.data.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByAccountNo(String accountNo);
  Optional<Account> findByAccountNoAndDeletedFalse(String accountNo);
}
