package dk.medes.bank.account;

import java.math.BigDecimal;

// Separate from the entity so the public contract doesn't expose the persistence model
public record AccountResponse(
        String accountNumber,
        String firstName,
        String lastName,
        BigDecimal balance) {

    public static AccountResponse from(Account account) {
        return new AccountResponse(
                account.getAccountNumber(),
                account.getFirstName(),
                account.getLastName(),
                account.getBalance());
    }
}
