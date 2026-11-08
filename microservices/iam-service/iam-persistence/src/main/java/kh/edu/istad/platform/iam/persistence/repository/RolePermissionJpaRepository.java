package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolePermissionJpaRepository extends JpaRepository<RolePermissionEntity, Long> {

    @Query("SELECT rp FROM RolePermissionEntity rp " +
            "WHERE rp.roleId = :roleId AND rp.revokedAt " +
            "IS NULL AND (rp.expiresAt IS NULL OR rp.expiresAt > CURRENT_TIMESTAMP)"
    )
    List<RolePermissionEntity> findActiveByRoleId(@Param("roleId") Long roleId);

    @Query("SELECT rp FROM RolePermissionEntity rp " +
            "WHERE rp.roleId = :roleId AND rp.permissionId = :permissionId " +
            "AND rp.revokedAt " +
            "IS NULL AND (rp.expiresAt IS NULL OR rp.expiresAt > CURRENT_TIMESTAMP)"
    )
    Optional<RolePermissionEntity> findActiveAssignment(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
}
