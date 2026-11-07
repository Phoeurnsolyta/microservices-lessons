package kh.edu.istad.platform.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record CreateRoleRequest(
        @NotBlank(message = "Role name is required")
        @Size(max = 50)
        String name,
        String description,
        List<UUID> permissionUuids
) {
}
