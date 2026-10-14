package kh.edu.istad.platform.business.domain.valueobject;

import java.util.List;

public record BusinessContact(
        List<String> emails,
        String phoneNumber
) {
    public BusinessContact {
        if (emails == null || emails.isEmpty()) {
            throw new IllegalArgumentException(
                    "At least one email is required"
            );
        }

        emails = emails.stream()
                .map(email -> {
                    if (email == null || email.isBlank()
                            || !email.matches(
                            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                    )) {
                        throw new IllegalArgumentException(
                                "Invalid business email address"
                        );
                    }

                    return email.trim();
                })
                .distinct()
                .toList();

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Business phone number cannot be blank"
            );
        }

        phoneNumber = phoneNumber.trim();
    }
}
