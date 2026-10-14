package kh.edu.istad.platform.business.restapi.dto;

import java.util.List;
import java.util.UUID;

public record BusinessRegisterResponse (
        UUID businessId,
        String legalName,
        String displayName,
        List<String> emails,
        String phoneNumber,
        boolean active
){
}
