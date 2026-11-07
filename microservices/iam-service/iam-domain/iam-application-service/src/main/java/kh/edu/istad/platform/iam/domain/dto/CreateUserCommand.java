package kh.edu.istad.platform.iam.domain.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateUserCommand(
        String username,
        String email,
        String password,
        String givenName,
        String familyName,
        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String zoneinfo,
        String locale,
        AddressCommand address,
        List<UUID> roleUuids
) {
}
