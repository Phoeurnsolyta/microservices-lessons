package kh.edu.istad.platform.iam.domain.dto;

import java.time.Instant;
import java.util.UUID;

public record IdentityResult(
        UUID uuid,
        String provider,
        String email,
        String displayName,
        Instant linkedAt
) {
}
