package kh.edu.istad.platform.iam.restapi.dto.request;

import jakarta.validation.Valid;

import java.time.LocalDate;

public record UpdateUserProfileRequest(
        String givenName,
        String familyName,
        String middleName,
        String nickname,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String zoneinfo,
        String locale,

        @Valid AddressRequest address
) {
}
