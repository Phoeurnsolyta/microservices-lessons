package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.DirectPermissionResult;
import kh.edu.istad.platform.iam.domain.dto.RoleAuthorityResult;
import kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult;
import kh.edu.istad.platform.iam.domain.entity.*;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.service.IamDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GetUserAuthoritiesUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final IamDomainService domainService;

    public UserAuthoritiesResult execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found: " + userUuid));

        Instant now = Instant.now();
        List<UserRole> userRoles = userRepository.findActiveRolesByUserId(user.getId());
        List<UserPermission> userPermissions = userRepository.findActivePermissionsByUserId(user.getId());

        Map<Long, Role> rolesById = new HashMap<>();
        List<RolePermission> rolePermissions = new ArrayList<>();

        List<RoleAuthorityResult> roleAuthorityResults = new ArrayList<>();
        for (UserRole ur : userRoles) {
            roleRepository.findById(ur.getRoleId()).ifPresent(role -> {
                rolesById.put(role.getId(), role);
                rolePermissions.addAll(roleRepository.findActivePermissionsByRoleId(role.getId()));
                roleAuthorityResults.add(new RoleAuthorityResult(role.getUuid(), role.getName(), ur.getExpiresAt()));
            });
        }

        Map<Long, Permission> permsById = new HashMap<>();

        for (RolePermission rp : rolePermissions) {
            permissionRepository.findById(rp.getPermissionId()).ifPresent(p -> permsById.put(p.getId(), p));
        }
        for (UserPermission up : userPermissions) {
            permissionRepository.findById(up.getPermissionId()).ifPresent(p -> permsById.put(p.getId(), p));
        }

        Set<String> effectivePermissions = domainService.computeEffectivePermissions(
                now, userRoles, rolesById, rolePermissions, userPermissions, permsById
        );

        List<DirectPermissionResult> directPermissions = new ArrayList<>();
        for (UserPermission up : userPermissions) {
            Permission p = permsById.get(up.getPermissionId());
            if (p != null) {
                directPermissions.add(new DirectPermissionResult(p.getName(), up.getEffect().name()));
            }
        }
        return new UserAuthoritiesResult(
                roleAuthorityResults,
                directPermissions,
                new ArrayList<>(effectivePermissions)
        );
    }
}
