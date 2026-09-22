package com.eren.wallet.ledger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eren.wallet.account.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ledger")
@RequiredArgsConstructor

public class LedgerController {

	private final AccountService accountService;

	@GetMapping("/reconciliation")
	public String reconciliation() {
		return accountService.isSystemBalanced() ? "BALANCED" : "MISTACH!";
	}
}
