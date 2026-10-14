package kh.edu.istad.platform.business.domain.dto;

import java.util.List;

public record RegisterBusinessCommand(
        String legalName,
        String displayName,
        List<String> emails,
        String phoneNumber
) {
}
