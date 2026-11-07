package kh.edu.istad.platform.iam.domain.usecase.role;

import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RevokePermissionFromRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public void execute(UUID roleUuid, UUID permissionUuid, String reason) {

        Role role = roleRepository.findByUuid(roleUuid)
                .orElseThrow(() -> new IamDomainException("Role not found"));

        role.validateModifiable();

        Permission perm = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new IamDomainException("Permission not found"));

        String actor = currentActorProvider.getCurrentActorSub();

        roleRepository.findActiveRolePermission(
                role.getId(),
                perm.getId()).ifPresent(rp -> {

            rp.revoke(actor, reason);

            roleRepository.saveRolePermission(rp);
                }
        );
    }
}
