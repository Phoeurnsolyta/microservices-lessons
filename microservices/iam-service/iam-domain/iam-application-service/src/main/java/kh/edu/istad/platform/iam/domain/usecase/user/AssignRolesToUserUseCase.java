package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.AssignRolesToUserCommand;
import kh.edu.istad.platform.iam.domain.dto.RoleAssignmentResult;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserRole;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.service.IamDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AssignRolesToUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamDomainService domainService;

    public List<RoleAssignmentResult> execute(AssignRolesToUserCommand command) {
        User user = userRepository.findByUuid(command.userUuid())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found"));

        String actor = currentActorProvider.getCurrentActorSub();

        Instant now = Instant.now();
        List<RoleAssignmentResult> results = new ArrayList<>();

        for (UUID roleUuid : command.roleUuids()) {
            Role role = roleRepository.findByUuid(roleUuid)
                    .orElseThrow(() -> new IamDomainException("Role not found: " + roleUuid));

            domainService.validateRoleAssignment(role);

            userRepository.findActiveUserRole(user.getId(),
                    role.getId()).ifPresent(active -> {
                        active.revoke(
                        actor, "Superseeded by new assignment"
                );
                userRepository.saveUserRole(active);
            });

            UserRole assignment = UserRole.builder()
                    .userId(user.getId())
                    .roleId(role.getId())
                    .assignedAt(now)
                    .assignedBy(actor)
                    .expiresAt(command.expiresAt())
                    .reason(command.reason())
                    .build();
            userRepository.saveUserRole(assignment);

            results.add(
                    new RoleAssignmentResult(
                            role.getUuid(),
                            role.getName(), now,
                            command.expiresAt()
                    )
            );
        }
        return results;
    }
}
