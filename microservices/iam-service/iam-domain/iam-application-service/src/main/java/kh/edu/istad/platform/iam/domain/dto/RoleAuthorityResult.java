package kh.edu.istad.platform.iam.domain.dto;

import java.time.Instant;
import java.util.UUID;

public record RoleAuthorityResult(
        UUID uuid,
        String name,
        Instant expiresAt
) {
}
