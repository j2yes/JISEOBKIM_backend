package com.jskim.banking.controller;

import com.jskim.banking.request.command.CreateAccountCommand;
import com.jskim.banking.request.command.DeleteAccountCommand;
import com.jskim.banking.request.query.AccountTransactionQuery;
import com.jskim.banking.response.AccountDTO;
import com.jskim.banking.response.AccountTransactionDTO;
import com.jskim.banking.service.AccountService;
import com.jskim.banking.util.response.CursorPage;
import com.jskim.banking.util.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

  private final AccountService accountService;

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Response<AccountDTO> create(@Valid @RequestBody CreateAccountCommand dto) {
    return Response.success(accountService.create(dto));
  }

  @DeleteMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Response<Void> delete(@Valid @RequestBody DeleteAccountCommand deleteAccountDTO) {
    accountService.delete(deleteAccountDTO);
    return Response.success();
  }

  @GetMapping(value = "/{accountNo}/transactions")
  public Response<CursorPage<AccountTransactionDTO, Long>> getTransactions(
      @PathVariable String accountNo, @Valid AccountTransactionQuery query) {
    CursorPage<AccountTransactionDTO, Long> page = accountService.getTransactions(accountNo, query);
    return Response.success(page);
  }
}
