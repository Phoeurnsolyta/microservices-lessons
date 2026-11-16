package kh.edu.istad.platform.iam.restapi.controller;

import kh.edu.istad.platform.iam.restapi.service.RedisMediaStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final RedisMediaStorageService mediaStorageService;

    @GetMapping("/avatars/{userUuid}")
    public ResponseEntity<byte[]> getAvatar(@PathVariable("userUuid") UUID userUuid) {
        byte[] imageBytes = mediaStorageService.getAvatar(userUuid);
        if (imageBytes == null || imageBytes.length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = mediaStorageService.getAvatarContentType(userUuid);
        MediaType mediaType = MediaType.IMAGE_JPEG;
        if (contentType != null) {
            try {
                mediaType = MediaType.parseMediaType(contentType);
            } catch (Exception ignored) {
            }
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                .body(imageBytes);
    }
}