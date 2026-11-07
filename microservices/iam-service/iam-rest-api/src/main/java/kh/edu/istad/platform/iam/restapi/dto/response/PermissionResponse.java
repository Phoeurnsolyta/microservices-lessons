package kh.edu.istad.platform.iam.restapi.dto.response;

import java.util.UUID;

public record PermissionResponse(
        UUID uuid,
        String name,
        String resource,
        String action,
        String description,
        boolean active,
        boolean isSystem
) {
}
