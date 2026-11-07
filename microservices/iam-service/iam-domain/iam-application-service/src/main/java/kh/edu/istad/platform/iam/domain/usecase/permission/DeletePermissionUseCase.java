package kh.edu.istad.platform.iam.domain.usecase.permission;

import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeletePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public void execute(UUID permissionUuid) {

        Permission p = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new IamDomainException("Permission not found"));

        p.softDelete(currentActorProvider.getCurrentActorSub());

        permissionRepository.save(p);
    }
}
