package kh.edu.istad.platform.payment.domain.valueobject;

import java.util.UUID;

public record CreditHistoryId(UUID value) {

    public CreditHistoryId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Credit history ID cannot be null"
            );
        }
    }
}
