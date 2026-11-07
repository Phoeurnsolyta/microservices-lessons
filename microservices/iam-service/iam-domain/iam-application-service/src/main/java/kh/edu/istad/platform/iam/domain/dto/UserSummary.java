package kh.edu.istad.platform.iam.domain.dto;

import java.util.UUID;

public record UserSummary(
        UUID uuid,
        String username,
        String email,
        String name,
        boolean enabled
) {
}
