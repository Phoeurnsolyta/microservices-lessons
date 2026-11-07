package kh.edu.istad.platform.iam.restapi.dto.request;

public record AddressRequest(
        String streetAddress,
        String locality,
        String region,
        String postalCode,
        String country
) {
}
