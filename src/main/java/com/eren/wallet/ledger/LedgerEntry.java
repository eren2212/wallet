package com.eren.wallet.ledger;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ledger_entries")
@Getter
@Setter
@NoArgsConstructor

public class LedgerEntry {

	@Id
	@GeneratedValue
	private UUID id;

	@Column(nullable = false)
	private UUID accountId;

	@Column(nullable = false, precision = 19, scale = 4)
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private EntryType type;

	@Column(nullable = false)
	private UUID transactionId;

	@Column(nullable = false, updatable = false)
	private Instant createdAt;

	void onCreate() {
		this.createdAt = Instant.now();
	}

	public enum EntryType {
		DEBIT, CREDIT
	}

}
