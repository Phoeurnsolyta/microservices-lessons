package kh.edu.istad.platform.business.domain.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public record Money (BigDecimal amount){

    public Money {
        Objects.requireNonNull(amount, "Money amount cannot be null");

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Money amount cannot be negative"
            );
        }
    }

    public static Money of(BigDecimal amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "Money cannot be null");
        return new Money(this.amount.add(other.amount));
    }
}
