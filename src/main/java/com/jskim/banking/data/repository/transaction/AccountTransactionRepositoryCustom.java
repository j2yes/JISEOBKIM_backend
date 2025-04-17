package com.jskim.banking.data.repository.transaction;

import com.jskim.banking.request.query.AccountTransactionQuery;
import com.jskim.banking.response.AccountTransactionDTO;
import com.jskim.banking.util.response.CursorPage;

public interface AccountTransactionRepositoryCustom {

  CursorPage<AccountTransactionDTO, Long> findAllByAccountNo(String accountNo,
      AccountTransactionQuery query);
}
