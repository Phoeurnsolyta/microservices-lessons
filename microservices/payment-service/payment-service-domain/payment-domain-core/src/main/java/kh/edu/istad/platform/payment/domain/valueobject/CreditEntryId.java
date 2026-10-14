package kh.edu.istad.platform.payment.domain.valueobject;

import java.util.UUID;

public record CreditEntryId(UUID value) {

    public CreditEntryId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Credit entry ID cannot be null"
            );
        }
    }
}
