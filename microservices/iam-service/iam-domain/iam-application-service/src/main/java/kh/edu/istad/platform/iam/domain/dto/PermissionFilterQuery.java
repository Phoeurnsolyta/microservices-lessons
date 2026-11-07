package kh.edu.istad.platform.iam.domain.dto;

public record PermissionFilterQuery(
        String resource,
        String search,
        int page,
        int size,
        String sort
) {
}
