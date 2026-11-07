package kh.edu.istad.platform.iam.restapi.dto.request;

import jakarta.validation.constraints.NotEmpty;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AssignPermissionsToRoleRequest(

        @NotEmpty
        List<UUID> permissionUuids,

        Instant expiresAt,
        String reason
) {
}
