package kh.edu.istad.platform.iam.restapi.dto.response;

import java.util.UUID;

public record UserRoleSummaryResponse(
        UUID uuid,
        String name
) {}

