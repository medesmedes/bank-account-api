package dk.medes.bank.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// We could add range validation e.g.
public record DepositRequest(@NotNull
                             @DecimalMin(value = "0.0", inclusive = false)
                             @Digits(integer = 19, fraction = 2)
                             BigDecimal amount) {
}
