package kh.edu.istad.platform.iam.restapi.service;

import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.restapi.dto.response.AuditLogResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.LoginHistoryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisLoginHistoryService {

    private final StringRedisTemplate redisTemplate;
    private final UserRepository userRepository;

    private static final String HISTORY_PREFIX = "login:history:";
    private static final String AUDIT_KEY = "audit:system:events";
    private static final String DELIMITER = "::";

    public void recordLogin(UUID userUuid, String status, String ipAddress, String device, String location, String reason) {
        try {
            String id = "lh-" + UUID.randomUUID().toString().substring(0, 8);
            Instant timestamp = Instant.now();
            String safeIp = (ipAddress != null && !ipAddress.isBlank()) ? ipAddress : "127.0.0.1";
            String safeDevice = (device != null && !device.isBlank()) ? device : "Web Browser";
            String safeLocation = (location != null && !location.isBlank()) ? location : "Phnom Penh, KH";
            String safeReason = (reason != null && !reason.isBlank()) ? reason : "Password Authentication";

            String raw = String.join(DELIMITER,
                    id,
                    timestamp.toString(),
                    status,
                    safeIp,
                    safeDevice,
                    safeLocation,
                    safeReason
            );

            String key = HISTORY_PREFIX + userUuid;
            redisTemplate.opsForList().leftPush(key, raw);
            redisTemplate.opsForList().trim(key, 0, 49);
            log.info("Recorded real login event in Redis for user {}: status={}", userUuid, status);
        } catch (Exception e) {
            log.error("Failed to record login history in Redis for user {}", userUuid, e);
        }
    }

    public List<LoginHistoryResponse> getLoginHistory(UUID userUuid) {
        String key = HISTORY_PREFIX + userUuid;
        List<String> rawList = redisTemplate.opsForList().range(key, 0, 49);

        if (rawList == null || rawList.isEmpty()) {
            return List.of();
        }

        List<LoginHistoryResponse> result = new ArrayList<>();
        for (String raw : rawList) {
            try {
                String[] parts = raw.split(DELIMITER, -1);
                if (parts.length >= 7) {
                    result.add(new LoginHistoryResponse(
                            parts[0],
                            Instant.parse(parts[1]),
                            parts[2],
                            parts[3],
                            parts[4],
                            parts[5],
                            parts[6]
                    ));
                }
            } catch (Exception e) {
                log.warn("Could not parse login history entry from Redis: {}", raw);
            }
        }
        return result;
    }

    public void recordAuditEvent(String eventType, String actor, String target, String details, String ipAddress, String status) {
        try {
            String id = "aud-" + UUID.randomUUID().toString().substring(0, 8);
            Instant timestamp = Instant.now();
            String safeIp = (ipAddress != null && !ipAddress.isBlank()) ? ipAddress : "127.0.0.1";
            if ("0:0:0:0:0:0:0:1".equals(safeIp)) safeIp = "127.0.0.1";
            String safeActor = (actor != null && !actor.isBlank()) ? actor : "system";
            String safeTarget = (target != null && !target.isBlank()) ? target : "System";
            String safeDetails = (details != null && !details.isBlank()) ? details : "";
            String safeStatus = (status != null && !status.isBlank()) ? status : "SUCCESS";

            String raw = String.join(DELIMITER,
                    id,
                    eventType,
                    safeActor,
                    safeTarget,
                    safeDetails,
                    safeIp,
                    safeStatus,
                    timestamp.toString()
            );

            redisTemplate.opsForList().leftPush(AUDIT_KEY, raw);
            redisTemplate.opsForList().trim(AUDIT_KEY, 0, 99); // Keep latest 100 genuine events
            log.info("Recorded real dynamic audit event: type={}, actor={}, status={}", eventType, safeActor, safeStatus);
        } catch (Exception e) {
            log.error("Failed to record audit event in Redis: {}", eventType, e);
        }
    }

    public List<AuditLogResponse> getAuditLogs(String eventType, String search) {
        List<String> rawList = redisTemplate.opsForList().range(AUDIT_KEY, 0, 99);

        if (rawList == null || rawList.isEmpty()) {
            try {
                Set<String> historyKeys = redisTemplate.keys(HISTORY_PREFIX + "*");
                if (historyKeys != null && !historyKeys.isEmpty()) {
                    for (String hKey : historyKeys) {
                        String userUuidStr = hKey.replace(HISTORY_PREFIX, "");
                        String username = userUuidStr;
                        try {
                            UUID uUuid = UUID.fromString(userUuidStr);
                            if (userRepository != null) {
                                username = userRepository.findByUuid(uUuid)
                                        .map(User::getUsername)
                                        .orElse(userUuidStr);
                            }
                        } catch (Exception ignored) {
                        }

                        List<String> userLogins = redisTemplate.opsForList().range(hKey, 0, 9);
                        if (userLogins != null) {
                            for (String rawLogin : userLogins) {
                                String[] parts = rawLogin.split(DELIMITER, -1);
                                if (parts.length >= 7) {
                                    String id = parts[0];
                                    String ts = parts[1];
                                    String status = parts[2];
                                    String ip = parts[3];
                                    String device = parts[4];
                                    String location = parts[5];
                                    String reason = parts[6];

                                    String eType = "SUCCESS".equalsIgnoreCase(status) ? "USER_LOGIN_SUCCESS" : "LOGIN_FAILURE";
                                    String details = reason + " via " + device + " (" + location + ")";

                                    String auditRaw = String.join(DELIMITER,
                                            id.replace("lh-", "aud-"),
                                            eType,
                                            username,
                                            "User: " + username,
                                            details,
                                            ip,
                                            status,
                                            ts
                                    );
                                    redisTemplate.opsForList().rightPush(AUDIT_KEY, auditRaw);
                                }
                            }
                        }
                    }
                    rawList = redisTemplate.opsForList().range(AUDIT_KEY, 0, 99);
                }
            } catch (Exception e) {
                log.warn("Failed to check existing login history in Redis", e);
            }
        }

        if (rawList == null || rawList.isEmpty()) {
            return List.of();
        }

        List<AuditLogResponse> result = new ArrayList<>();
        String normalizedFilter = (eventType != null && !eventType.isBlank() && !"ALL".equalsIgnoreCase(eventType))
                ? eventType.trim().toUpperCase()
                : null;
        String normalizedSearch = (search != null && !search.isBlank())
                ? search.trim().toLowerCase()
                : null;

        for (String raw : rawList) {
            try {
                String[] parts = raw.split(DELIMITER, -1);
                if (parts.length >= 8) {
                    String id = parts[0];
                    String type = parts[1];
                    String actor = parts[2];
                    String target = parts[3];
                    String details = parts[4];
                    String ip = parts[5];
                    String status = parts[6];
                    Instant ts = Instant.parse(parts[7]);

                    if (normalizedFilter != null && !type.equalsIgnoreCase(normalizedFilter)) {
                        continue;
                    }

                    if (normalizedSearch != null) {
                        boolean match = actor.toLowerCase().contains(normalizedSearch)
                                || target.toLowerCase().contains(normalizedSearch)
                                || details.toLowerCase().contains(normalizedSearch)
                                || type.toLowerCase().contains(normalizedSearch);
                        if (!match) {
                            continue;
                        }
                    }

                    result.add(new AuditLogResponse(id, type, actor, target, details, ip, status, ts));
                }
            } catch (Exception e) {
                log.warn("Could not parse audit log entry from Redis: {}", raw);
            }
        }
        return result;
    }
}