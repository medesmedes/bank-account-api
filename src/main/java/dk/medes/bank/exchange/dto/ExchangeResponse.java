package dk.medes.bank.exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record ExchangeResponse(
        @JsonProperty("DKK") BigDecimal dkk,
        @JsonProperty("USD") BigDecimal usd) {
}
