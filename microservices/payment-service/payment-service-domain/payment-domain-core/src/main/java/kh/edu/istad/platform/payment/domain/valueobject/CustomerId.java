package kh.edu.istad.platform.payment.domain.valueobject;

import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Customer ID cannot be null"
            );
        }
    }
}
