package kh.edu.istad.platform.iam.restapi.dto.response;

public record TokenResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        String redirectUrl
) {
    public TokenResponse(String accessToken, String tokenType, long expiresIn) {
        this(accessToken, tokenType, expiresIn, null);
    }
}