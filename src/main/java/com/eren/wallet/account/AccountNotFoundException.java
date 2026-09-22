package com.eren.wallet.account;

public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException(java.util.UUID accountId) {
		super("Hesap bulunamadı: " + accountId);
	}
}