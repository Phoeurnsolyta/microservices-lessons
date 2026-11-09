package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.CreateUserCommand;
import kh.edu.istad.platform.iam.domain.dto.RoleSummary;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserRole;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.CurrentActorProvider;
import kh.edu.istad.platform.iam.domain.port.out.PasswordHasher;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.service.IamDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final RoleRepository roleRepository;
    private final CurrentActorProvider currentActorProvider;
    private final IamDomainService iamDomainService;

    public UserResult execute(CreateUserCommand command) {
        log.info("Creating user with username: {}", command.username());

        if (userRepository.existsByUsername(command.username())) {
            throw new IamDomainException("Username already exists");
        }
        if (userRepository.existsByEmail(command.email().toLowerCase())) {
            throw new IamDomainException("Email already exists");
        }

        String actor = currentActorProvider.getCurrentActorSub();
        Instant now = Instant.now();

        User.Builder builder = User.builder()
                .uuid(UUID.randomUUID())
                .username(command.username())
                .email(command.email().toLowerCase())
                .emailVerified(false)
                .passwordHash(passwordHasher.encode(command.password()))
                .givenName(command.givenName())
                .familyName(command.familyName())
                .gender(command.gender())
                .birthdate(command.birthdate())
                .phoneNumber(command.phoneNumber())
                .zoneinfo(command.zoneinfo())
                .locale(command.locale())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .failedLoginAttempts(0)
                .isDeleted(false)
                .createdAt(now)
                .createdBy(actor != null ? actor : "system")
                .updatedAt(now)
                .updatedBy(actor != null ? actor : "system");

        if (command.address() != null) {
            builder.addressStreetAddress(command.address().streetAddress())
                    .addressLocality(command.address().locality())
                    .addressRegion(command.address().region())
                    .addressPostalCode(command.address().postalCode())
                    .addressCountry(command.address().country());
        }

        User savedUser = userRepository.save(builder.build());

        List<RoleSummary> roleSummaries = new ArrayList<>();
        if (command.roleUuids() != null) {
            for (UUID roleUuid : command.roleUuids()) {
                Role role = roleRepository.findByUuid(roleUuid)
                        .orElseThrow(() -> new IamDomainException("Role not found: " + roleUuid));
                iamDomainService.validateRoleAssignment(role);

                UserRole userRole = UserRole.builder()
                        .userId(savedUser.getId())
                        .roleId(role.getId())
                        .assignedAt(now)
                        .assignedBy(actor != null ? actor : "system")
                        .reason("Assigned during user creation")
                        .build();
                userRepository.saveUserRole(userRole);

                roleSummaries.add(new RoleSummary(role.getUuid(), role.getName(), role.getDescription(), role.isActive()));
            }
        }

        return new UserResult(
                savedUser.getUuid(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.isEmailVerified(),
                savedUser.getFullName(),
                savedUser.getGivenName(),
                savedUser.getFamilyName(),
                savedUser.getLocale(),
                savedUser.isEnabled(),
                roleSummaries,
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }
}