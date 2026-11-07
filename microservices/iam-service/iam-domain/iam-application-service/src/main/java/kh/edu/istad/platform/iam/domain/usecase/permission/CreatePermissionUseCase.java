package kh.edu.istad.platform.iam.domain.usecase.permission;

import kh.edu.istad.platform.iam.domain.dto.CreatePermissionCommand;
import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreatePermissionUseCase {

    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public PermissionResult execute(CreatePermissionCommand command) {

        String permName = Permission.computeName(
                command.resource(),
                command.action()
        );

        if (permissionRepository.existsByName(permName)) {
            throw new IamDomainException("Permission already exists: " + permName);
        }

        String actor = currentActorProvider.getCurrentActorSub();

        Permission permission = Permission.builder()
                .uuid(UUID.randomUUID())
                .name(permName)
                .resource(command.resource().trim().toLowerCase())
                .action(command.action().trim().toLowerCase())
                .description(command.description())
                .isSystem(false)
                .isActive(true)
                .createdBy(actor)
                .updatedBy(actor)
                .build();

        Permission saved = permissionRepository.save(permission);

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
