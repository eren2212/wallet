package com.eren.wallet.account;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eren.wallet.ledger.LedgerEntry;
import com.eren.wallet.ledger.LedgerEntryRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class AccountService {

	private final AccountRepository accountRepository;
	private final LedgerEntryRepository ledgerEntryRepository;

	@Transactional
	public Account createAccount(String ownerName, String currency) {
		Account account = new Account();
		account.setOwnerName(ownerName);
		account.setCurrency(currency);

		return accountRepository.save(account);
	}

	@Transactional
	public void deposit(UUID accountId, BigDecimal amount) {
		if (amount.signum() <= 0) {
			throw new IllegalArgumentException("Yatırılan tutar pozitif olmalı");
		}

		Optional<Account> optional = accountRepository.findById(accountId);
		if (optional.isEmpty()) {
			new IllegalArgumentException("Hesap bulunamadı: " + accountId);
		}

		Account account = optional.get();

		LedgerEntry entry = new LedgerEntry();
		entry.setAccountId(account.getId());
		entry.setAmount(amount);
		entry.setType(LedgerEntry.EntryType.CREDIT);
		entry.setTransactionId(UUID.randomUUID());

		ledgerEntryRepository.save(entry);

	}

	public BigDecimal getBalance(UUID accountId) {
		return ledgerEntryRepository.calcuateBalance(accountId);
	}
}
