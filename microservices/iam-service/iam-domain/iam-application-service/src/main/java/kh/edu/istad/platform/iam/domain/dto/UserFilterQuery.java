package kh.edu.istad.platform.iam.domain.dto;

public record UserFilterQuery(
        String search,
        Boolean enabled,
        int page,
        int size,
        String sort
) {
}
