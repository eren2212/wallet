package com.eren.wallet.account;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.eren.wallet.account.dto.AccountResponse;
import com.eren.wallet.account.dto.CreateAccountRequest;
import com.eren.wallet.account.dto.DepositRequest;
import com.eren.wallet.account.dto.TransferRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public AccountResponse create(@RequestBody CreateAccountRequest request) {
		Account account = accountService.createAccount(request.ownerName(), request.currency());

		return new AccountResponse(account.getId(), account.getOwnerName(), account.getCurrency());
	}

	@PostMapping("/{id}/deposit")
	public void deposit(@PathVariable UUID id, @RequestBody DepositRequest request) {
		accountService.deposit(id, request.amount());
	}

	@GetMapping("/{id}/balance")
	public BigDecimal getBalance(@PathVariable UUID id) {
		return accountService.getBalance(id);
	}

	@PostMapping("/{id}/transfer")
	public void transfer(@PathVariable UUID id, @RequestBody TransferRequest request) {
		accountService.transfer(id, request.toAccountId(), request.amount());
	}
}
