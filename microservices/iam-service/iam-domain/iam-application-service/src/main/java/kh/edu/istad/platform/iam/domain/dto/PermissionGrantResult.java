package kh.edu.istad.platform.iam.domain.dto;

import java.time.Instant;
import java.util.UUID;

public record PermissionGrantResult(
        UUID permissionUuid,
        String permissionName,
        String effect,
        Instant expiresAt
) {
}
