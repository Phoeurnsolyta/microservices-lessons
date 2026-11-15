package kh.edu.istad.platform.iam.restapi.dto.response;

import java.time.Instant;

public record UserSessionResponse(
        String id,
        String device,
        String browser,
        String ipAddress,
        String location,
        Instant lastActive,
        boolean isCurrent
) {
}