package kh.edu.istad.platform.business.restapi.dto;

import jakarta.validation.constraints.*;

public record BusinessRegisterRequest(
        @NotBlank
        @Size(max = 200)
        String legalName,

        @NotBlank
        @Size(max = 100)
        String displayName,

        @NotBlank
        @Email String email,

        @NotBlank
        @Pattern(regexp = "^[0-9+()\\-\\s]{7,20}$")
        String phoneNumber
) {
}
