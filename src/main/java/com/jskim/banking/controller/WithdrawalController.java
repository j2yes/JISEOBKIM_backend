package com.jskim.banking.controller;

import com.jskim.banking.request.command.WithdrawCommand;
import com.jskim.banking.response.AccountDTO;
import com.jskim.banking.service.WithdrawalService;
import com.jskim.banking.util.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/withdrawals")
@RequiredArgsConstructor
public class WithdrawalController {

  private final WithdrawalService withdrawalService;

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Response<AccountDTO> withdraw(@Valid @RequestBody WithdrawCommand dto) {
    return Response.success(withdrawalService.withdraw(dto));
  }
}
