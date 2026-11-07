package kh.edu.istad.platform.iam.domain.usecase.permission;

import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.dto.UpdatePermissionCommand;
import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public PermissionResult execute(UpdatePermissionCommand command) {

        Permission p = permissionRepository.findByUuid(command.permissionUuid())
                .filter(perm -> !perm.isDeleted())
                .orElseThrow(() -> new IamDomainException("Permission not found"));

        p.updateDescription(command.description(), currentActorProvider.getCurrentActorSub());
        Permission saved = permissionRepository.save(p);

        return new PermissionResult(
                saved.getUuid(),
                saved.getName(),
                saved.getResource(),
                saved.getAction(),
                saved.getDescription(),
                saved.isActive(),
                saved.isSystem()
        );
    }
}
