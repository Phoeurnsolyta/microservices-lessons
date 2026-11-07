package kh.edu.istad.platform.iam.restapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePermissionRequest(
        @NotBlank(message = "Resource is required")
        @Size(max = 50)
        String resource,
        @NotBlank(message = "Action is required")
        @Size(max = 50)
        String action,
        @Size(max = 255)
        String description
) {
}
