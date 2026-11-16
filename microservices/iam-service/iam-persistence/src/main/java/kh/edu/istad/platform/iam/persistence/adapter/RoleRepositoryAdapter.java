package kh.edu.istad.platform.iam.persistence.adapter;

import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.RoleFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.RoleSummary;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.RolePermission;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import kh.edu.istad.platform.iam.persistence.entity.RoleEntity;
import kh.edu.istad.platform.iam.persistence.entity.RolePermissionEntity;
import kh.edu.istad.platform.iam.persistence.mapper.RolePersistenceMapper;
import kh.edu.istad.platform.iam.persistence.repository.RoleJpaRepository;
import kh.edu.istad.platform.iam.persistence.repository.RolePermissionJpaRepository;
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
public class RoleRepositoryAdapter implements RoleRepository {

    private final RoleJpaRepository roleJpaRepository;
    private final RolePermissionJpaRepository rolePermissionJpaRepository;
    private final RolePersistenceMapper rolePersistenceMapper;

    @Override
    public Role save(Role role) {
        RoleEntity entity = rolePersistenceMapper.toEntity(role);

        entity.setActive(role.isActive());
        entity.setSystem(role.isSystem());
        entity.setDeleted(role.isDeleted());

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
        RoleEntity saved = roleJpaRepository.save(entity);
        return mapToDomainWithFlags(saved);
    }

    private Role mapToDomainWithFlags(RoleEntity entity) {
        if (entity == null) return null;
        Role domain = rolePersistenceMapper.toDomain(entity);
        return Role.builder()
                .id(domain.getId())
                .uuid(domain.getUuid())
                .name(domain.getName())
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
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id).map(this::mapToDomainWithFlags);
    }

    @Override
    public Optional<Role> findByUuid(UUID uuid) {
        return roleJpaRepository.findByUuid(uuid).map(this::mapToDomainWithFlags);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name).map(this::mapToDomainWithFlags);
    }

    @Override
    public boolean existsByName(String name) {
        return roleJpaRepository.existsByName(name);
    }

    @Override
    public PageResult<RoleSummary> findAll(RoleFilterQuery query) {
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        if (query.sort() != null && !query.sort().isBlank()) {
            String[] parts = query.sort().split(",");
            String property = parts[0].trim();
            Sort.Direction direction = parts.length > 1 && parts[1].trim().equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sort = Sort.by(direction, property);
        }

        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);
        Page<RoleEntity> page = roleJpaRepository.findAll(pageable);

        List<RoleSummary> content = page.getContent().stream()
                .map(e -> new RoleSummary(
                        e.getUuid(),
                        e.getName(),
                        e.getDescription(),
                        e.isActive()
                ))
                .toList();

        return new PageResult<>(
                content,
                new PageResult.PageMetadata(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages())
        );
    }

    @Override
    public List<RolePermission> findActivePermissionsByRoleId(Long roleId) {
        return rolePermissionJpaRepository.findActiveByRoleId(roleId).stream()
                .map(rolePersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<RolePermission> findActiveRolePermission(Long roleId, Long permissionId) {
        return rolePermissionJpaRepository.findActiveAssignment(roleId, permissionId)
                .map(rolePersistenceMapper::toDomain);
    }

    @Override
    public void saveRolePermission(RolePermission rolePermission) {
        RolePermissionEntity entity = rolePersistenceMapper.toEntity(rolePermission);
        rolePermissionJpaRepository.save(entity);
    }
}
