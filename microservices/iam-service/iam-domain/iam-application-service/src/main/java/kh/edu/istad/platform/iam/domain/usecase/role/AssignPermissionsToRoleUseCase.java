package kh.edu.istad.platform.iam.domain.usecase.role;

import kh.edu.istad.platform.iam.domain.dto.AssignPermissionsToRoleCommand;
import kh.edu.istad.platform.iam.domain.dto.PermissionAssignmentResult;
import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.RolePermission;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.domain.service.IamDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AssignPermissionsToRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamDomainService domainService;

    public List<PermissionAssignmentResult> execute(AssignPermissionsToRoleCommand command) {
        Role role = roleRepository.findByUuid(command.roleUuid())
                .filter(r -> !r.isDeleted())
                .orElseThrow(() -> new IamDomainException("Role not found"));
        role.validateModifiable();

        domainService.validateRoleAssignment(role);

        String actor = currentActorProvider.getCurrentActorSub();
        Instant now = Instant.now();

        List<PermissionAssignmentResult> results = new ArrayList<>();
        for (UUID pUuid : command.permissionUuids()) {
            Permission perm = permissionRepository.findByUuid(pUuid)
                    .orElseThrow(() -> new IamDomainException("Permission not found: " + pUuid));

            domainService.validatePermissionAssignment(perm);

            roleRepository.findActiveRolePermission(
                    role.getId(),
                    perm.getId()).ifPresent(active -> {
                active.revoke(
                        actor, "Superseeded by new role permission assignment");
                roleRepository.saveRolePermission(active);
            });

            RolePermission rp = RolePermission.builder()
                    .roleId(role.getId())
                    .permissionId(perm.getId())
                    .assignedAt(now)
                    .assignedBy(actor)
                    .expiresAt(command.expiresAt())
                    .reason(command.reason())
                    .build();
            roleRepository.saveRolePermission(rp);
            results.add(
                    new PermissionAssignmentResult(
                            perm.getUuid(),
                            perm.getName(),
                            now,
                            command.expiresAt())
            );
        }
        return results;
    }
}
