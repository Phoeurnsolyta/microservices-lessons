package kh.edu.istad.platform.iam.domain.usecase.role;

import kh.edu.istad.platform.iam.domain.dto.CreateRoleCommand;
import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.dto.RoleResult;
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
public class CreateRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamDomainService domainService;

    public RoleResult execute(CreateRoleCommand command) {

        String roleName = command.name().trim().toUpperCase();

        if (roleRepository.existsByName(roleName)) {
            throw new IamDomainException("Role already exists: " + roleName);
        }

        String actor = currentActorProvider.getCurrentActorSub();
        Role role = Role.builder()
                .uuid(UUID.randomUUID())
                .name(roleName)
                .description(command.description())
                .isSystem(false)
                .isActive(true)
                .createdBy(actor)
                .updatedBy(actor)
                .build();

        Role savedRole = roleRepository.save(role);

        List<PermissionResult> assignedPerms = new ArrayList<>();
        if (command.permissionUuids() != null) {
            for (UUID pUuid : command.permissionUuids()) {
                Permission p = permissionRepository.findByUuid(pUuid)
                        .orElseThrow(() -> new IamDomainException("Permission not found: " + pUuid));

                domainService.validatePermissionAssignment(p);
                RolePermission rp = RolePermission.builder()
                        .roleId(savedRole.getId())
                        .permissionId(p.getId())
                        .assignedAt(Instant.now())
                        .assignedBy(actor)
                        .reason("Assigned during role creation")
                        .build();

                roleRepository.saveRolePermission(rp);

                assignedPerms.add(
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
        }
        return new RoleResult(
                savedRole.getUuid(),
                savedRole.getName(),
                savedRole.getDescription(),
                savedRole.isSystem(),
                savedRole.isActive(),
                assignedPerms
        );
    }
}
