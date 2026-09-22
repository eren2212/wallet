package com.eren.wallet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.eren.wallet.account.AccountNotFoundException;

@RestControllerAdvice

public class GlobalExceptionHandler {

	@ExceptionHandler(AccountNotFoundException.class)
	public ResponseEntity<String> handleNotFound(AccountNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<String> handleConflictRequest(IllegalStateException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
	}
}
