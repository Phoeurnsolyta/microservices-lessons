package kh.edu.istad.platform.order.domain.valueobject;

import java.util.UUID;

public record StreetAddress(
        UUID value,
        String street,
        String postalCode,
        String city
) {
}
