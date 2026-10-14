package kh.edu.istad.platform.business.restapi.dto;

import java.util.UUID;

public record BusinessRegisterResponse (
        UUID businessId,
        String legalName,
        String displayName,
        String emails,
        String phoneNumber,
        boolean active
){
}
