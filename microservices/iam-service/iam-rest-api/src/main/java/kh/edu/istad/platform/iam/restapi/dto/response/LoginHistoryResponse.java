package kh.edu.istad.platform.iam.restapi.dto.response;

import java.time.Instant;

public record LoginHistoryResponse(
        String id,
        Instant timestamp,
        String status,
        String ipAddress,
        String device,
        String location,
        String reason
) {
}