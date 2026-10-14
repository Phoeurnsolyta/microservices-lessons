package kh.edu.istad.platform.business.restapi.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record BusinessRegisterRequest(
        @NotBlank
        @Size(max = 200)
        String legalName,

        @NotBlank
        @Size(max = 100)
        String displayName,

        @NotEmpty
        List<@NotBlank @Email String> emails,

        @NotBlank
        @Pattern(regexp = "^[0-9+()\\-\\s]{7,20}$")
        String phoneNumber
) {
}
