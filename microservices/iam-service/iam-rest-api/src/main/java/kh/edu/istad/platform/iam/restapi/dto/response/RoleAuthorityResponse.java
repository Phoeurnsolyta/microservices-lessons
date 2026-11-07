package kh.edu.istad.platform.iam.restapi.dto.response;

import java.time.Instant;
import java.util.UUID;

public record RoleAuthorityResponse(
        UUID uuid,
        String name,
        Instant expiresAt
) {
}
