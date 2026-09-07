package dk.medes.bank.account;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.math.BigDecimal;

@ApplicationScoped
public class AccountService {

    @Inject
    AccountRepository repository;

    @ConfigProperty(name = "bank.identifier")
    String bankIdentifier;

    @Transactional
    public Account createAccount(String firstName, String lastName) {
        // Real account numbers are generated from a series of identifiers and checks
        String accountNumber = bankIdentifier + String.format("%08d", repository.nextAccountNumber());
        Account account = new Account(accountNumber, firstName, lastName);
        repository.persist(account);
        return account;
    }

    public Account getByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account " + accountNumber + " not found"));
    }

    private Account getByAccountNumberWithLock(String accountNumber) {
        return repository.findByAccountNumberWithLock(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account " + accountNumber + " not found"));
    }

    @Transactional
    public Account deposit(String accountNumber, BigDecimal amount) {
        Account account = getByAccountNumberWithLock(accountNumber);
        account.deposit(amount);
        return account;
    }

    @Transactional
    public Account transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new BadRequestException("Cannot transfer to the same account");
        }
        // Lock account to lowest numbers first, so that if two transactions were attempting to modify the same account
        // one would always lock out the other first
        Account from;
        Account to;
        if (fromAccountNumber.compareTo(toAccountNumber) < 0) {
            from = getByAccountNumberWithLock(fromAccountNumber);
            to = getByAccountNumberWithLock(toAccountNumber);
        } else {
            to = getByAccountNumberWithLock(toAccountNumber);
            from = getByAccountNumberWithLock(fromAccountNumber);
        }
        from.withdraw(amount); // InsufficientFundsException rolls the whole transfer back
        to.deposit(amount);
        return from;
    }

}
