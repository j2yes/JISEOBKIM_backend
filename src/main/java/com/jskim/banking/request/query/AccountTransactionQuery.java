package com.jskim.banking.request.query;

import com.jskim.banking.data.code.AccountTransactionType;
import com.jskim.banking.util.request.CursorPageQuery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountTransactionQuery extends CursorPageQuery<Long> {

  private AccountTransactionType eventType;
}
