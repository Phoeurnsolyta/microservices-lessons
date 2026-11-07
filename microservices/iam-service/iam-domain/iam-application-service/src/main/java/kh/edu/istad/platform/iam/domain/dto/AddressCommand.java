package kh.edu.istad.platform.iam.domain.dto;

public record AddressCommand(
        String streetAddress,
        String locality,
        String region,
        String postalCode,
        String country
) {
}
