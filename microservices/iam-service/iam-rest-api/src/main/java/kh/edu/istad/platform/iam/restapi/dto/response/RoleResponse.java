package kh.edu.istad.platform.iam.restapi.dto.response;

import java.util.UUID;

public record RoleResponse(
        UUID uuid,
        String name,
        String description,
        boolean active,
        boolean isSystem,
        List<PermissionResponse> permissions
) {
}
