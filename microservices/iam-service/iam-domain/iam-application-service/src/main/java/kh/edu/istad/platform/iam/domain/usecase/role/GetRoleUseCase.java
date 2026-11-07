package kh.edu.istad.platform.iam.domain.usecase.role;

import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.dto.RoleResult;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.RolePermission;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleResult execute(UUID roleUuid) {
        Role role = roleRepository.findByUuid(roleUuid)
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IamDomainException("Role not found: " + roleUuid));

        Instant now = Instant.now();

        List<RolePermission> rolePermissions = roleRepository
                .findActivePermissionsByRoleId(role.getId());

        List<PermissionResult> permissions = new ArrayList<>();

        for (RolePermission rp : rolePermissions) {
            if (rp.isActive(now)) {
                permissionRepository.findById(
                        rp.getPermissionId()
                ).ifPresent(p -> {
                    if (p.isActive() && !p.isDeleted()) {
                        permissions.add(
                                new PermissionResult(
                                        p.getUuid(),
                                        p.getName(),
                                        p.getResource(),
                                        p.getAction(),
                                        p.getDescription(),
                                        p.isActive(),
                                        p.isSystem())
                        );
                    }
                });
            }
        }
        return new RoleResult(
                role.getUuid(),
                role.getName(),
                role.getDescription(),
                role.isSystem(),
                role.isActive(),
                permissions
        );
    }
}
