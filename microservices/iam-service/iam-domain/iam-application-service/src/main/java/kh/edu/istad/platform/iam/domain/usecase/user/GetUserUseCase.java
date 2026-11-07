package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.RoleSummary;
import kh.edu.istad.platform.iam.domain.dto.UserResult;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserRole;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
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
public class GetUserUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserResult execute(UUID userUuid) {

        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found: " + userUuid));

        Instant now = Instant.now();

        List<UserRole> activeUserRoles = userRepository.findActiveRolesByUserId(user.getId());
        List<RoleSummary> roles = new ArrayList<>();
        for (UserRole ur : activeUserRoles) {
            if (ur.isActive(now)) {
                roleRepository.findById(ur.getRoleId()).ifPresent(r -> {
                    if (r.isActive() && !r.isDeleted()) {
                        roles.add(new RoleSummary(r.getUuid(), r.getName(), r.getDescription(), r.isActive()));
                    }
                });
            }
        }

        return new UserResult(
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.isEmailVerified(),
                user.getFullName(),
                user.getGivenName(),
                user.getFamilyName(),
                user.getLocale(),
                user.isEnabled(), roles,
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
