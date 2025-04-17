package com.jskim.banking.request.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WithdrawCommand {

  @NotBlank
  private String accountNo;
  @NotNull
  @Positive
  private BigDecimal amount;
}
