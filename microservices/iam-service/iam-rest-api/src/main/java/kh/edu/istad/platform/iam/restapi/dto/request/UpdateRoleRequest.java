package kh.edu.istad.platform.iam.restapi.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateRoleRequest(
        @Size(max = 255)
        String description
) {
}
