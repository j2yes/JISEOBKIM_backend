package com.jskim.banking.controller;

import com.jskim.banking.request.command.TransferCommand;
import com.jskim.banking.response.AccountDTO;
import com.jskim.banking.service.TransferService;
import com.jskim.banking.util.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
public class TransferController {

  private final TransferService transferService;

  @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
  public Response<AccountDTO> transfer(@Valid @RequestBody TransferCommand dto) {
    return Response.success(transferService.transfer(dto));
  }
}
