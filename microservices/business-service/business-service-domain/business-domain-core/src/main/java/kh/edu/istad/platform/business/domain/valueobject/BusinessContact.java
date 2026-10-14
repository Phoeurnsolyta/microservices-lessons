package kh.edu.istad.platform.business.domain.valueobject;

public record BusinessContact(
        String email,
        String phoneNumber
) {
    public BusinessContact {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Business email cannot be blank"
            );
        }

        email = email.trim();

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )) {
            throw new IllegalArgumentException(
                    "Invalid business email address"
            );
        }

        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException(
                    "Business phone number cannot be blank"
            );
        }

        phoneNumber = phoneNumber.trim();
    }
}
