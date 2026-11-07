package kh.edu.istad.platform.iam.domain.dto;

import java.time.Instant;
import java.util.UUID;

public record RoleAssignmentResult(
        UUID roleUuid,
        String roleName,
        Instant assignedAt,
        Instant expiresAt
) {
}
