package kh.edu.istad.platform.iam.domain.usecase.permission;

import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GetPermissionUseCase {

    private final PermissionRepository permissionRepository;

    public PermissionResult execute(UUID permissionUuid) {

        Permission p = permissionRepository.findByUuid(permissionUuid)
                .filter(perm -> !perm.isDeleted())
                .orElseThrow(() -> new IamDomainException("Permission not found: " + permissionUuid));

        return new PermissionResult(
                p.getUuid(),
                p.getName(),
                p.getResource(),
                p.getAction(),
                p.getDescription(),
                p.isActive(),
                p.isSystem()
        );
    }
}
