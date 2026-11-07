package kh.edu.istad.platform.iam.domain.usecase.permission;

import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ActivatePermissionUseCase {

    private final PermissionRepository permissionRepository;

    public void execute(UUID permissionUuid) {

        Permission p = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new IamDomainException("Permission not found"));
        p.validateModifiable();

        Permission updated = Permission.builder()
                .id(p.getId())
                .uuid(p.getUuid())
                .name(p.getName())
                .resource(p.getResource())
                .action(p.getAction())
                .description(p.getDescription())
                .isSystem(p.isSystem())
                .isActive(true)
                .build();

        permissionRepository.save(updated);
    }
}
