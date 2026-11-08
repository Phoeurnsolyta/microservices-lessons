package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolePermissionJpaRepository extends JpaRepository<RolePermissionEntity, Long> {

    List<RolePermissionEntity> findActiveByRoleId(Long roleId);

    Optional<RolePermissionEntity> findActiveAssignment(Long roleId, Long permissionId);
}
