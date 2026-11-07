package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.GrantPermissionsToUserCommand;
import kh.edu.istad.platform.iam.domain.dto.PermissionGrantResult;
import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserPermission;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.service.IamDomainService;
import kh.edu.istad.platform.iam.domain.valueobject.PermissionEffect;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class GrantPermissionsToUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamDomainService domainService;

    public List<PermissionGrantResult> execute(GrantPermissionsToUserCommand command) {

        User user = userRepository.findByUuid(command.userUuid())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found"));

        String actor = currentActorProvider.getCurrentActorSub();
        Instant now = Instant.now();
        PermissionEffect effect = PermissionEffect.valueOf(command.effect().toUpperCase());

        List<PermissionGrantResult> results = new ArrayList<>();
        for (UUID permUuid : command.permissionUuids()) {

            Permission perm = permissionRepository.findByUuid(permUuid)
                    .orElseThrow(() -> new IamDomainException("Permission not found: " + permUuid));

            domainService.validatePermissionAssignment(perm);

            userRepository.findActiveUserPermission(
                    user.getId(),
                    perm.getId()).ifPresent(active -> {

                active.revoke(actor, "Superseeded by new direct permission grant");
                userRepository.saveUserPermission(active);
            });

            UserPermission assignment = UserPermission.builder()
                    .userId(user.getId())
                    .permissionId(perm.getId())
                    .effect(effect)
                    .assignedAt(now)
                    .assignedBy(actor)
                    .expiresAt(command.expiresAt())
                    .reason(command.reason())
                    .build();

            userRepository.saveUserPermission(assignment);

            results.add(
                    new PermissionGrantResult(
                            perm.getUuid(),
                            perm.getName(),
                            effect.name(),
                            command.expiresAt())
            );
        }

        return results;
    }
}
