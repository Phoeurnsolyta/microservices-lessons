package kh.edu.istad.platform.iam.persistence.adapter;

import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.PermissionFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import kh.edu.istad.platform.iam.persistence.entity.PermissionEntity;
import kh.edu.istad.platform.iam.persistence.mapper.PermissionPersistenceMapper;
import kh.edu.istad.platform.iam.persistence.repository.PermissionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PermissionRepositoryAdapter implements PermissionRepository {

    private final PermissionJpaRepository permissionJpaRepository;
    private final PermissionPersistenceMapper permissionPersistenceMapper;

    @Override
    public Permission save(Permission permission) {
        PermissionEntity entity = permissionPersistenceMapper.toEntity(permission);

        entity.setActive(permission.isActive());
        entity.setSystem(permission.isSystem());
        entity.setDeleted(permission.isDeleted());

        Instant now = Instant.now();
        if (entity.getCreatedAt() == null) {
            entity.setCreatedAt(now);
        }
        if (entity.getUpdatedAt() == null) {
            entity.setUpdatedAt(now);
        }
        if (entity.getCreatedBy() == null || entity.getCreatedBy().isBlank()) {
            entity.setCreatedBy("admin");
        }
        if (entity.getUpdatedBy() == null || entity.getUpdatedBy().isBlank()) {
            entity.setUpdatedBy("admin");
        }
        PermissionEntity saved = permissionJpaRepository.save(entity);
        return mapToDomainWithFlags(saved);
    }

    private Permission mapToDomainWithFlags(PermissionEntity entity) {
        if (entity == null) return null;
        Permission domain = permissionPersistenceMapper.toDomain(entity);
        return Permission.builder()
                .id(domain.getId())
                .uuid(domain.getUuid())
                .name(domain.getName())
                .resource(domain.getResource())
                .action(domain.getAction())
                .description(domain.getDescription())
                .isSystem(entity.isSystem())
                .isActive(entity.isActive())
                .isDeleted(entity.isDeleted())
                .deletedAt(domain.getDeletedAt())
                .deletedBy(domain.getDeletedBy())
                .createdAt(domain.getCreatedAt())
                .createdBy(domain.getCreatedBy())
                .updatedAt(domain.getUpdatedAt())
                .updatedBy(domain.getUpdatedBy())
                .version(domain.getVersion())
                .build();
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return permissionJpaRepository.findById(id).map(this::mapToDomainWithFlags);
    }

    @Override
    public Optional<Permission> findByUuid(UUID uuid) {
        return permissionJpaRepository.findByUuid(uuid).map(this::mapToDomainWithFlags);
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return permissionJpaRepository.findByName(name).map(this::mapToDomainWithFlags);
    }

    @Override
    public boolean existsByName(String name) {
        return permissionJpaRepository.existsByName(name);
    }

    @Override
    public PageResult<PermissionResult> findAll(PermissionFilterQuery query) {

        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        if (query.sort() != null && !query.sort().isBlank()) {
            String[] parts = query.sort().split(",");
            String property = parts[0].trim();
            Sort.Direction direction = parts.length > 1 && parts[1]
                    .trim().equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sort = Sort.by(direction, property);
        }

        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

        Page<PermissionEntity> page;
        boolean hasSearch = query.search() != null && !query.search().isBlank();
        boolean hasResource = query.resource() != null && !query.resource().isBlank();

        if (hasSearch) {
            String searchPattern = "%" + query.search().trim().toLowerCase() + "%";
            page = permissionJpaRepository.searchPermissions(
                    hasResource ? query.resource().trim() : null,
                    searchPattern,
                    pageable
            );
        } else if (hasResource) {
            page = permissionJpaRepository.findByResourceAndIsDeletedFalse(query.resource().trim(), pageable);
        } else {
            page = permissionJpaRepository.findByIsDeletedFalse(pageable);
        }

        List<PermissionResult> content = page.getContent().stream()
                .map(e -> new PermissionResult(
                        e.getUuid(),
                        e.getName(),
                        e.getResource(),
                        e.getAction(),
                        e.getDescription(),
                        e.isActive(),
                        e.isSystem()
                ))
                .toList();

        return new PageResult<>(
                content,
                new PageResult.PageMetadata(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages()
                )
        );
    }
}
