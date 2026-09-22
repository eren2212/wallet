package com.eren.wallet.account;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.eren.wallet.ledger.LedgerEntry;
import com.eren.wallet.ledger.LedgerEntry.EntryType;
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
			throw new AccountNotFoundException(accountId);
		}

		Account account = optional.get();

		LedgerEntry entry = new LedgerEntry();
		entry.setAccountId(account.getId());
		entry.setAmount(amount);
		entry.setType(LedgerEntry.EntryType.CREDIT);
		entry.setTransactionId(UUID.randomUUID());

		ledgerEntryRepository.save(entry);

	}

	@Transactional
	public void transfer(UUID fromAccountId, UUID toAccountId, BigDecimal amount) {

		if (amount.signum() <= 0) {
			throw new IllegalArgumentException("Transfer tutarı pozitif olmalı");
		}

		if (fromAccountId.equals(toAccountId)) {
			throw new IllegalArgumentException("Aynı hesaba transfer yapılamaz");
		}

		Optional<Account> fromOptional = accountRepository.findById(fromAccountId);
		Account fromAccount = fromOptional.get();

		if (fromOptional.isEmpty()) {
			throw new AccountNotFoundException(fromAccountId);
		}

		Optional<Account> toOptional = accountRepository.findById(toAccountId);
		Account toAccount = toOptional.get();

		if (toOptional.isEmpty()) {
			throw new AccountNotFoundException(toAccountId);
		}

		// BigDecimal'de <, >, == operatörleri çalışmaz (primitive değil, nesne).
		// compareTo() -1/0/1 döner: senderBalance < amount ise -1 döner, yani bakiye
		// yetersizdir.
		BigDecimal senderBalanca = ledgerEntryRepository.calcuateBalance(fromAccountId);
		if (senderBalanca.compareTo(amount) < 0) {
			throw new IllegalStateException("Yetersiz Bakiye");
		}

		UUID transactionId = UUID.randomUUID();

		LedgerEntry debit = new LedgerEntry();
		debit.setAccountId(fromAccountId);
		debit.setAmount(amount);
		debit.setTransactionId(transactionId);
		debit.setType(EntryType.DEBIT);
		ledgerEntryRepository.save(debit);

		LedgerEntry credit = new LedgerEntry();
		credit.setAccountId(toAccountId);
		credit.setAmount(amount);
		credit.setTransactionId(transactionId);
		credit.setType(EntryType.CREDIT);
		ledgerEntryRepository.save(credit);
	}

	public boolean isSystemBalanced() {
		return ledgerEntryRepository.sumAllDebits().compareTo(ledgerEntryRepository.sumAllCredits()) == 0;
	}

	public BigDecimal getBalance(UUID accountId) {
		return ledgerEntryRepository.calcuateBalance(accountId);
	}
}
