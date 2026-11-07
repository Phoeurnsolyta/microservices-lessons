package kh.edu.istad.platform.iam.restapi.dto.response;

import kh.edu.istad.platform.iam.domain.dto.PageResult;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        PageResult.PageMetadata page
) {
    public record PageMetadata(
            int number,
            int size,
            long totalElements,
            int totalPages
    ) {}
}
