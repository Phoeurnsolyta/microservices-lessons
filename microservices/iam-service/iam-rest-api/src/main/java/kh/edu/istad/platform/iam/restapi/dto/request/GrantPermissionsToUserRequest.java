package kh.edu.istad.platform.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record GrantPermissionsToUserRequest(
        @NotEmpty(message = "At least one permission UUID is required")
        List<UUID> permissionUuids,

        @NotBlank(message = "Effect is required")
        @Pattern(regexp = "^(GRANT|DENY)$", message = "Effect must be GRANT or DENY")
        String effect,

        Instant expiresAt,
        String reason
) {
}
