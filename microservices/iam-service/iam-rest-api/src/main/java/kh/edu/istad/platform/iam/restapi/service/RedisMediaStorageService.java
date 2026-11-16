package kh.edu.istad.platform.iam.restapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisMediaStorageService {

    private final StringRedisTemplate redisTemplate;

    private static final String AVATAR_PREFIX = "media:avatar:";
    private static final String CONTENT_TYPE_PREFIX = "media:avatar:type:";

    public void saveAvatar(UUID userUuid, byte[] data, String contentType) {
        String base64Data = Base64.getEncoder().encodeToString(data);
        redisTemplate.opsForValue().set(AVATAR_PREFIX + userUuid, base64Data);
        if (contentType != null && !contentType.isBlank()) {
            redisTemplate.opsForValue().set(CONTENT_TYPE_PREFIX + userUuid, contentType);
        }
        log.info("Saved avatar in Redis for user {}", userUuid);
    }

    public byte[] getAvatar(UUID userUuid) {
        String base64 = redisTemplate.opsForValue().get(AVATAR_PREFIX + userUuid);
        if (base64 == null || base64.isBlank()) {
            return null;
        }
        return Base64.getDecoder().decode(base64);
    }

    public String getAvatarContentType(UUID userUuid) {
        String type = redisTemplate.opsForValue().get(CONTENT_TYPE_PREFIX + userUuid);
        return type != null ? type : "image/jpeg";
    }

    public void deleteAvatar(UUID userUuid) {
        redisTemplate.delete(List.of(
                AVATAR_PREFIX + userUuid,
                CONTENT_TYPE_PREFIX + userUuid
        ));
        log.info("Deleted avatar from Redis for user {}", userUuid);
    }
}