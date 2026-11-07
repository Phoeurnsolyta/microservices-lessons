package kh.edu.istad.platform.iam.domain.dto;

import java.util.List;

public record PageResult<T>(
        List<T> content,
        PageMetadata page
) {
    public record PageMetadata(int number, int size, long totalElements, int totalPages) {

    }
}
