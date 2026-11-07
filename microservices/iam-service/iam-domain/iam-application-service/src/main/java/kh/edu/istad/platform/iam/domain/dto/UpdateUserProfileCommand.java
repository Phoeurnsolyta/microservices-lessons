package kh.edu.istad.platform.iam.domain.dto;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateUserProfileCommand(
        UUID userUuid,
        String givenName,
        String familyName,
        String middleName,
        String nickname,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String zoneinfo,
        String locale,
        AddressCommand address
) {
}
