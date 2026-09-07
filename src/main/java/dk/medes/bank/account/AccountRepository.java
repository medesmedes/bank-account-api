package dk.medes.bank.account;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.LockModeType;

import java.util.Optional;

// Repository over active-record for testability: injected, so mockable
@ApplicationScoped
public class AccountRepository implements PanacheRepository<Account> {

    public Optional<Account> findByAccountNumber(String accountNumber) {
        return find("accountNumber", accountNumber).firstResultOptional();
    }

    // Pessimistic write lock for read-modify-write on the balance (deposit, transfer)
    public Optional<Account> findByAccountNumberWithLock(String accountNumber) {
        return find("accountNumber", accountNumber)
                .withLock(LockModeType.PESSIMISTIC_WRITE)
                .firstResultOptional();
    }

    // Next value from the dedicated account-number sequence (defined in import.sql)
    public long nextAccountNumber() {
        return ((Number) getEntityManager()
                .createNativeQuery("select next value for account_number_seq")
                .getSingleResult()).longValue();
    }
}
