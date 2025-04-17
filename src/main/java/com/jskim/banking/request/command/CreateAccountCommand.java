package com.jskim.banking.request.command;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccountCommand {

  @NotBlank
  private String owner;
}
