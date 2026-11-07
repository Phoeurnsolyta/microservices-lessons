package kh.edu.istad.platform.iam.domain.dto;

import java.util.List;
import java.util.UUID;

public record AuthUserResult(
        Long id,
        UUID uuid,
        String username,
        String email,
        String passwordHash,
        boolean enabled,
        boolean accountNonExpired,
        boolean accountNonLocked,
        boolean credentialsNonExpired,
        List<String> authorities
) {
}
