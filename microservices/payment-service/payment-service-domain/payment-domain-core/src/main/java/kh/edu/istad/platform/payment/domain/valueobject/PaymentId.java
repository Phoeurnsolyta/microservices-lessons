package kh.edu.istad.platform.payment.domain.valueobject;

import java.util.UUID;

public record PaymentId(UUID value) {

    public PaymentId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Payment ID cannot be null"
            );
        }
    }
}
