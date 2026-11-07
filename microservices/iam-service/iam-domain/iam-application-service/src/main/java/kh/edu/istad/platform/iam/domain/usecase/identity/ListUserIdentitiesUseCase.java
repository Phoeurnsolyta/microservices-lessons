package kh.edu.istad.platform.iam.domain.usecase.identity;

import kh.edu.istad.platform.iam.domain.dto.IdentityResult;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ListUserIdentitiesUseCase {

    private final UserRepository userRepository;

    public List<IdentityResult> execute(UUID userUuid) {
        User user = userRepository.findByUuid(userUuid)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("User not found"));

        return userRepository.findIdentitiesByUserId(user.getId()).stream()
                .filter(i -> !i.isDeleted())
                .map(i -> new IdentityResult(
                        i.getUuid(),
                        i.getProvider().name(),
                        i.getEmail(),
                        i.getDisplayName(),
                        i.getLinkedAt())
                ).collect(Collectors.toList());
    }
}
