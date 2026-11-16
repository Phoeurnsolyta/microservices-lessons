package kh.edu.istad.platform.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CreateOAuth2ClientRequest(
        @NotBlank String clientId,
        @NotBlank String clientName,
        @NotBlank String clientSecret,
        List<String> grantTypes,
        List<String> scopes,
        List<String> redirectUris
) {
}