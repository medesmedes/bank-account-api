package dk.medes.bank.shared;

import java.math.BigDecimal;
import java.math.RoundingMode;

// Added to ensure that we always use two decimal places
// Is intended to be complemented by validation in the application, domain and data layers
public final class Money {
    private static final int SCALE = 2;
    // HALF_EVEN to reduce bias
    private static final RoundingMode ROUNDING = RoundingMode.HALF_EVEN;

    // Used when the value comes from within the project e.g. mock data, test fixtures, constants
    public static BigDecimal of(String value) {
        return new BigDecimal(value).setScale(SCALE, ROUNDING);
    }
    // Used when the source of the value is external via deserialization or requests
    // Don't use on operands or else we lose precision
    public static BigDecimal normalize(BigDecimal value) {
        return value.setScale(SCALE, ROUNDING);
    }
    private Money() {}
}