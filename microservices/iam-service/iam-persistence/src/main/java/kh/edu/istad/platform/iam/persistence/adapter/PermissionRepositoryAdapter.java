package kh.edu.istad.platform.iam.persistence.adapter;

import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.PermissionFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.domain.port.out.PermissionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PermissionRepositoryAdapter implements PermissionRepository {
    @Override
    public Permission save(Permission permission) {
        return null;
    }

    @Override
    public Optional<Permission> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Permission> findByUuid(UUID uuid) {
        return Optional.empty();
    }

    @Override
    public Optional<Permission> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean existsByName(String name) {
        return false;
    }

    @Override
    public PageResult<PermissionResult> findAll(PermissionFilterQuery query) {
        return null;
    }
}
