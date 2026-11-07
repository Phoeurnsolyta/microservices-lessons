package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RevokePermissionFromUserUseCase {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;
    private final CurrentActorProvider currentActorProvider;

    public void execute(UUID userUuid, UUID permissionUuid, String reason) {

        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new IamDomainException("User not found"));

        Permission perm = permissionRepository.findByUuid(permissionUuid)
                .orElseThrow(() -> new IamDomainException("Permission not found"));

        String actor = currentActorProvider.getCurrentActorSub();

        userRepository.findActiveUserPermission(
                user.getId(),
                perm.getId()).ifPresent(up -> {

            up.revoke(actor, reason);

            userRepository.saveUserPermission(up);
        }
        );
    }
}

