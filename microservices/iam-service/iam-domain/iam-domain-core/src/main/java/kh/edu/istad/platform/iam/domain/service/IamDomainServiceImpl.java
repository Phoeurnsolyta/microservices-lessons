package kh.edu.istad.platform.iam.domain.service;

import kh.edu.istad.platform.iam.domain.entity.*;
import kh.edu.istad.platform.iam.domain.exception.InactiveAssignmentException;
import kh.edu.istad.platform.iam.domain.exception.LastAuthenticationMethodRemovalException;
import kh.edu.istad.platform.iam.domain.valueobject.PermissionEffect;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class IamDomainServiceImpl implements IamDomainService {

    public static final String ROLE_PREFIX = "ROLE_";

    @Override
    public Set<String> computeEffectivePermissions(
            Instant evaluationTime,
            List<UserRole> activeUserRoles,
            Map<Long, Role> rolesById,
            List<RolePermission> rolePermissions,
            List<UserPermission> userPermissions,
            Map<Long, Permission> permissionsById
    ) {
        Set<Long> activeRoleIds = activeUserRoles.stream()
                .filter(ur -> ur.isActive(evaluationTime))
                .map(UserRole::getRoleId)
                .filter(rId -> {
                    Role role = rolesById.get(rId);
                    return role != null && role.isActive() && !role.isDeleted();
                })
                .collect(Collectors.toSet());

        Set<String> rolePermissionNames = rolePermissions.stream()
                .filter(rp -> rp.isActive(evaluationTime))
                .filter(rp -> activeRoleIds.contains(rp.getRoleId()))
                .map(rp -> permissionsById.get(rp.getPermissionId()))
                .filter(perm -> perm != null && perm.isActive() && !perm.isDeleted())
                .map(Permission::getName)
                .collect(Collectors.toSet());
        Set<String> effective = new HashSet<>(rolePermissionNames);

        userPermissions.stream()
                .filter(up -> up.isActive(evaluationTime))
                .filter(up -> up.getEffect() == PermissionEffect.GRANT)
                .map(up -> permissionsById.get(up.getPermissionId()))
                .filter(perm -> perm != null && perm.isActive() && !perm.isDeleted())
                .map(Permission::getName)
                .forEach(effective::add);

        userPermissions.stream()
                .filter(up -> up.isActive(evaluationTime))
                .filter(up -> up.getEffect() == PermissionEffect.DENY)
                .map(up -> permissionsById.get(up.getPermissionId()))
                .filter(Objects::nonNull)
                .map(Permission::getName)
                .forEach(effective::remove);
        return effective;
    }

    @Override
    public Set<String> computeAuthorities(
            Collection<Role> activeRoles,
            Set<String> effectivePermissions
    ) {
        Set<String> authorities = new HashSet<>();
        for (Role role : activeRoles) {
            authorities.add(ROLE_PREFIX + role.getName().toUpperCase());
        }
        authorities.addAll(effectivePermissions);
        return authorities;
    }

    @Override
    public void validateRoleAssignment(Role role) {
        if (!role.isActive() || role.isDeleted()) {
            throw new InactiveAssignmentException("Cannot assign inactive or deleted role: " + role.getName());
        }
    }

    @Override
    public void validatePermissionAssignment(Permission permission) {
        if (!permission.isActive() || permission.isDeleted()) {
            throw new InactiveAssignmentException("Cannot assign inactive or deleted permission: " + permission.getName());
        }
    }

    @Override
    public void validateIdentityUnlink(User user, List<Identity> activeIdentities, Long identityIdToUnlink) {
        boolean hasPassword = user.hasPassword();
        long remainingIdentities = activeIdentities.stream()
                .filter(id -> !id.getId().equals(identityIdToUnlink) && !id.isDeleted())
                .count();
        if (!hasPassword && remainingIdentities == 0) {
            throw new LastAuthenticationMethodRemovalException();
        }
    }
}
