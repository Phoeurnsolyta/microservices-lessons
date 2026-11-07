package kh.edu.istad.platform.iam.domain.dto;

import java.util.List;

public record UserAuthoritiesResult(
        List<RoleAuthorityResult> roles,
        List<DirectPermissionResult> directPermissions,
        List<String> effectivePermissions
) {
}
