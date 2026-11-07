package kh.edu.istad.platform.iam.domain.dto;

import java.util.UUID;

public record PermissionResult(
        UUID uuid,
        String name,
        String resource,
        String action,
        String description,
        boolean active,
        boolean isSystem
) {
}
