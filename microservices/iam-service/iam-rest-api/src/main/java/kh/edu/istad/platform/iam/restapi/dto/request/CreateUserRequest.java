package kh.edu.istad.platform.iam.restapi.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateUserRequest(
        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50)
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Must be a well-formed email address")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8)
        String password,

        @NotBlank(message = "Given name is required")
        String givenName,

        @NotBlank(message = "Family name is required")
        String familyName,

        String gender,
        LocalDate birthdate,
        String phoneNumber,
        String zoneinfo,
        String locale,

        @Valid AddressRequest address,
        List<UUID> roleUuids
) {
}
