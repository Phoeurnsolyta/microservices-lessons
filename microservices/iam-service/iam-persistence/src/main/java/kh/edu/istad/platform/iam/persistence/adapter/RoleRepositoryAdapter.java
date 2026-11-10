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
        return rolePersistenceMapper.toDomain(roleJpaRepository.save(entity));
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleJpaRepository.findById(id).map(rolePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Role> findByUuid(UUID uuid) {
        return roleJpaRepository.findByUuid(uuid).map(rolePersistenceMapper::toDomain);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleJpaRepository.findByName(name).map(rolePersistenceMapper::toDomain);
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
                .map(rolePersistenceMapper::toSummary)
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
