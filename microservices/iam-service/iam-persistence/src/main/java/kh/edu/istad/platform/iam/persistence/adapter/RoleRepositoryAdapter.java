package kh.edu.istad.platform.iam.persistence.adapter;

import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.RoleFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.RoleSummary;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.RolePermission;
import kh.edu.istad.platform.iam.domain.port.out.RoleRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class RoleRepositoryAdapter implements RoleRepository {
    @Override
    public Role save(Role role) {
        return null;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Role> findByUuid(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Role> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public PageResult<RoleSummary> findAll(RoleFilterQuery query) {
        return null;
    }

    @Override
    public List<RolePermission> findActivePermissionsByRoleId(Long roleId) {
        return List.of();
    }

    @Override
    public Optional<RolePermission> findActiveRolePermission(Long roleId, Long permissionId) {
        return Optional.empty();
    }

    @Override
    public void saveRolePermission(RolePermission rolePermission) {

    }
}
