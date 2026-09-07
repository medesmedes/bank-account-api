package dk.medes.bank.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank String toAccountNumber,
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @Digits(integer = 19, fraction = 2)
        BigDecimal amount) {
}
