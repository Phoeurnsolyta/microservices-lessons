package kh.edu.istad.platform.iam.restapi.dto.response;

public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn
) {
}