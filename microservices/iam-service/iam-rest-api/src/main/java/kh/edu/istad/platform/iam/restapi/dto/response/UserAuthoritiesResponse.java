package kh.edu.istad.platform.iam.restapi.dto.response;

import java.util.List;

public record UserAuthoritiesResponse(
        List<RoleAuthorityResponse> roles,
        List<DirectPermissionResponse> directPermissions,
        List<String> effectivePermissions
) {
}
