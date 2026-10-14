package kh.edu.istad.platform.business.domain.valueobject;

import java.util.UUID;

public record OrderApprovalId(UUID value) {

    public OrderApprovalId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Order approval ID cannot be null"
            );
        }
    }

    public static OrderApprovalId generate() {
        return new OrderApprovalId(UUID.randomUUID());
    }
}
