package kh.edu.istad.platform.iam.restapi.dto.response;

import java.time.Instant;

public record AuditLogResponse(
        String id,
        String eventType,
        String actor,
        String target,
        String details,
        String ipAddress,
        String status,
        Instant timestamp
) {
}