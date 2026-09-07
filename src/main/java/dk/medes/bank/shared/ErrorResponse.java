package dk.medes.bank.shared;

// Stable error shape so clients get structured errors, not raw stack traces (considerations.md §6)
public record ErrorResponse(String message) {
}
