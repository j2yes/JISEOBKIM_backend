package com.jskim.banking.data.repository.transaction;

import static com.jskim.banking.data.entity.QAccount.account;
import static com.jskim.banking.data.entity.QAccountTransaction.accountTransaction;
import com.jskim.banking.request.query.AccountTransactionQuery;
import com.jskim.banking.response.AccountTransactionDTO;
import com.jskim.banking.response.QAccountTransactionDTO;
import com.jskim.banking.util.response.CursorPage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;

public class AccountTransactionRepositoryImpl implements AccountTransactionRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  public AccountTransactionRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  @Override
  public CursorPage<AccountTransactionDTO, Long> findAllByAccountNo(String accountNo,
      AccountTransactionQuery query) {
    BooleanBuilder whereCondition = getWhereCondition(query);

    JPAQuery<AccountTransactionDTO> jpaQuery = jpaQueryFactory.select(
            new QAccountTransactionDTO(
                accountTransaction.id,
                accountTransaction.amount,
                accountTransaction.accountTransactionType,
                accountTransaction.transactionDateTime
            )
        ).from(accountTransaction)
        .innerJoin(accountTransaction.account, account)
        .where(whereCondition.and(account.accountNo.eq(accountNo)))
        .orderBy(accountTransaction.id.desc())
        .limit(query.getSize() + 1);

    return newCursorPage(jpaQuery, query);
  }

  private CursorPage<AccountTransactionDTO, Long> newCursorPage(
      JPAQuery<AccountTransactionDTO> jpaQuery, AccountTransactionQuery query) {
    List<AccountTransactionDTO> pageList = jpaQuery.fetch();

    boolean hasNext = pageList.size() > query.getSize();
    Long nextCursor = null;

    if (hasNext) {
      nextCursor = pageList.get(pageList.size() - 1).getId();
      pageList.remove(pageList.size() - 1);  // 다음 커서에 해당하는 마지막 데이터 삭제
    }

    return new CursorPage<>(pageList, nextCursor, hasNext);
  }

  private BooleanBuilder getWhereCondition(AccountTransactionQuery query) {
    BooleanBuilder builder = new BooleanBuilder();

    if (query.getEventType() != null) {
      builder.and(accountTransaction.accountTransactionType.eq(query.getEventType()));
    }

    if (query.getCursor() != null) {
      builder.and(accountTransaction.id.loe(query.getCursor()));
    }

    return builder;
  }
}
