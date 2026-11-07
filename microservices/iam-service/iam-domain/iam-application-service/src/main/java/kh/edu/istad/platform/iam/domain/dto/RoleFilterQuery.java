package kh.edu.istad.platform.iam.domain.dto;

public record RoleFilterQuery(
        String search,
        Boolean active,
        int page,
        int size,
        String sort
) {
}
