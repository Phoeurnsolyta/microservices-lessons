package kh.edu.istad.platform.business.domain.valueobject;

public record BusinessName(
        String legalName,
        String displayName
) {

    public BusinessName {
        if (legalName == null || legalName.isBlank()) {
            throw new IllegalArgumentException(
                    "Business legal name cannot be blank"
            );
        }

        if (displayName == null || displayName.isBlank()) {
            throw new IllegalArgumentException(
                    "Business display name cannot be blank"
            );
        }

        legalName = legalName.trim();
        displayName = displayName.trim();
    }
}
