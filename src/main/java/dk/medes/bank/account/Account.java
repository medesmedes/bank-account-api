package dk.medes.bank.account;

import dk.medes.bank.shared.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Business id, generated from a dedicated sequence, separate from the technical PK
    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    // Naive representation of balance
    // A real bank would probably represent this as a sum of all deposits and withdrawals
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    protected Account() {
        // required by JPA
    }

    public Account(String accountNumber, String firstName, String lastName) {
        this.accountNumber = accountNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.balance = Money.of("0");
    }

    public void deposit(BigDecimal amount) {
        requirePositive(amount);
        this.balance = Money.normalize(this.balance.add(amount));
    }

    public void withdraw(BigDecimal amount) {
        requirePositive(amount);
        if (this.balance.subtract(amount).signum() < 0) {
            throw new InsufficientFundsException("Insufficient funds on account " + accountNumber);
        }
        this.balance = Money.normalize(this.balance.subtract(amount));
    }

    // Validation on multiple layers
    private static void requirePositive(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    public Long getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
