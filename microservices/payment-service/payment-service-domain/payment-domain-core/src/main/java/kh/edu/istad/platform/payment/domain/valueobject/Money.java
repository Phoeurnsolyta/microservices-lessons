package kh.edu.istad.platform.payment.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal amount) {

    public Money {
        Objects.requireNonNull(amount, "Money amount cannot be null");

        amount = amount.setScale(2, RoundingMode.UNNECESSARY);

        if (amount.signum() < 0) {
            throw new IllegalArgumentException(
                    "Money amount cannot be negative"
            );
        }
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO.setScale(2));
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "Other money cannot be null");
        return new Money(amount.add(other.amount));
    }

    public Money subtract(Money other) {
        Objects.requireNonNull(other, "Other money cannot be null");

        if (amount.compareTo(other.amount) < 0) {
            throw new IllegalArgumentException(
                    "Insufficient balance"
            );
        }

        return new Money(amount.subtract(other.amount));
    }

    public boolean isZero() {
        return amount.signum() == 0;
    }

    public boolean isGreaterThan(Money other) {
        Objects.requireNonNull(other, "Other money cannot be null");
        return amount.compareTo(other.amount) > 0;
    }
}
