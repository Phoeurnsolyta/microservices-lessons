package kh.edu.istad.platform.business.domain.dto;

import java.util.List;
import java.util.UUID;

public record RegisterBusinessResult(
        UUID businessId,
        String legalName,
        String displayName,
        List<String> emails,
        String phoneNumber,
        boolean active
) {
}
