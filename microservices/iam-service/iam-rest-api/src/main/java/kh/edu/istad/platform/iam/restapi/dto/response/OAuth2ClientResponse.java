package kh.edu.istad.platform.iam.restapi.dto.response;

import java.util.List;

public record OAuth2ClientResponse(
        String id,
        String clientId,
        String clientName,
        String clientSecret,
        List<String> grantTypes,
        List<String> scopes,
        List<String> redirectUris
) {
}