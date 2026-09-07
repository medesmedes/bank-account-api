package dk.medes.bank.account.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateAccountRequest(@NotBlank String firstName, @NotBlank String lastName) {
}
