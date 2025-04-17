package com.jskim.banking.data.repository.transaction;

import com.jskim.banking.data.entity.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long>,
    AccountTransactionRepositoryCustom {

}
