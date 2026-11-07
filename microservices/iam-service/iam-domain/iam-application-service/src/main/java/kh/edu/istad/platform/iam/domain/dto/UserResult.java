package kh.edu.istad.platform.iam.domain.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UserResult(
        UUID uuid,
        String username,
        String email,
        boolean emailVerified,
        String name,
        String givenName,
        String familyName,
        String locale,
        boolean enabled,
        List<RoleSummary> roles,
        Instant createdAt,
        Instant updatedAt
) {
}
