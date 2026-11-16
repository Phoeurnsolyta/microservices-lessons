package kh.edu.istad.platform.iam.restapi.controller;

import kh.edu.istad.platform.iam.restapi.dto.response.AuditLogResponse;
import kh.edu.istad.platform.iam.restapi.service.RedisLoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final RedisLoginHistoryService loginHistoryService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<AuditLogResponse> getAuditLogs(
            @RequestParam(name = "eventType", required = false) String eventType,
            @RequestParam(name = "search", required = false) String search
    ) {
        return loginHistoryService.getAuditLogs(eventType, search);
    }
}