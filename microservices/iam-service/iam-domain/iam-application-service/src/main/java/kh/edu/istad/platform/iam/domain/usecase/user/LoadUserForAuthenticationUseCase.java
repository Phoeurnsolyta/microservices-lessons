package kh.edu.istad.platform.iam.domain.usecase.user;

import kh.edu.istad.platform.iam.domain.dto.AuthUserResult;
import kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.exception.IamDomainException;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoadUserForAuthenticationUseCase {

    private final UserRepository userRepository;
    private final GetUserAuthoritiesUseCase getUserAuthoritiesUseCase;

    public AuthUserResult execute(String usernameOrEmail) {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail.toLowerCase())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IamDomainException("Bad credentials"));

        UserAuthoritiesResult authoritiesResult = getUserAuthoritiesUseCase.execute(user.getUuid());

        List<String> combinedAuthorities = new ArrayList<>();
        authoritiesResult.roles().forEach(r -> combinedAuthorities.add("ROLE_" + r.name().toUpperCase()));
        combinedAuthorities.addAll(authoritiesResult.effectivePermissions());

        return new AuthUserResult(
                user.getId(),
                user.getUuid(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired(),
                combinedAuthorities
        );
    }
}
