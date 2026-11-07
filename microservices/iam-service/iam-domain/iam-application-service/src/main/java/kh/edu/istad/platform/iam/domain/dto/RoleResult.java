package kh.edu.istad.platform.iam.domain.dto;

import java.util.List;
import java.util.UUID;

public record RoleResult(
        UUID uuid,
        String name,
        String description,
        boolean isSystem,
        boolean active,
        List<PermissionResult> permissions
) {
}
