package com.eren.wallet.account.dto;

import java.util.UUID;

public record AccountResponse(UUID id, String ownerName, String currency) {

}

/*
 * Neden record? Java 14+'ta gelen bir özellik: sadece veri taşıyan, değişmez
 * (immutable) sınıflar için tasarlanmış. class yazıp
 * constructor/getter/equals/hashCode elle yazmak yerine tek satırda hepsini
 * alıyorsun. DTO'lar için birebir doğru araç, çünkü zaten sadece veri
 * taşıyorlar, davranışları yok.
 */