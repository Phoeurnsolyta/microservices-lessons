package kh.edu.istad.platform.iam.domain.service;

import kh.edu.istad.platform.iam.domain.entity.*;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IamDomainService {

    Set<String> computeEffectivePermissions(
            Instant evaluationTime,
            List<UserRole> activeUserRoles,
            Map<Long, Role> rolesById,
            List<RolePermission> rolePermissions,
            List<UserPermission> userPermissions,
            Map<Long, Permission> permissionsById
    );

    Set<String> computeAuthorities(
            Collection<Role> activeRoles,
            Set<String> effectivePermissions
    );

    void validateRoleAssignment(Role role);

    void validatePermissionAssignment(Permission permission);

    void validateIdentityUnlink(User user, List<Identity> activeIdentities, Long identityIdToUnlink);
}
