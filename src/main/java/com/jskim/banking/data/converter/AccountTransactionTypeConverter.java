package com.jskim.banking.data.converter;

import com.jskim.banking.data.code.AccountTransactionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountTransactionTypeConverter implements AttributeConverter<AccountTransactionType, String> {

  @Override
  public String convertToDatabaseColumn(AccountTransactionType accountTransactionType) {
    return accountTransactionType.getDbCode();
  }

  @Override
  public AccountTransactionType convertToEntityAttribute(String dbCode) {
    if (dbCode == null) {
      return null;
    }
    return AccountTransactionType.of(dbCode);
  }
}
