package kh.edu.istad.platform.iam.domain.dto;

import java.time.Instant;
import java.util.UUID;

public record PermissionAssignmentResult(
        UUID permissionUuid,
        String permissionName,
        Instant assignedAt,
        Instant expiresAt
) {
}
