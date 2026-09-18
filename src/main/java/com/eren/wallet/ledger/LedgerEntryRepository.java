package com.eren.wallet.ledger;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

	List<LedgerEntry> findByAccountIdOrderByCreatedAtAsc(UUID accountId);

	@Query("""
			SELECT COALESCE (SUM(CASE WHEN e.type = 'CREDIT' THEN e.amount ELSE -e.amount END ), 0
			FROM  LedgerEntry e WHERE e.accoundId = :accountId
			""")
	BigDecimal calcuateBalance(@Param("accountId") UUID accountId);

}
