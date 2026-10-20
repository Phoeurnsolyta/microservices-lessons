package kh.edu.istad.platform.customer.domain.valueobject;

import java.util.UUID;

public record CustomerStatusStatistic(
        CustomerStatus status,
        UUID segmentId,
        long total
) {
}
