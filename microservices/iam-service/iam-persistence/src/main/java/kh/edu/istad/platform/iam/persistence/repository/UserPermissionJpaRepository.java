package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserPermissionJpaRepository extends JpaRepository<UserPermissionEntity, Long> {

    @Query("SELECT up FROM UserPermissionEntity up " +
            "WHERE up.userId = :userId AND up.revokedAt " +
            "IS NULL AND (up.expiresAt IS NULL OR up.expiresAt > CURRENT_TIMESTAMP)"
    )
    List<UserPermissionEntity> findActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT up FROM UserPermissionEntity up " +
            "WHERE up.userId = :userId AND up.permissionId = :permissionId " +
            "AND up.revokedAt " +
            "IS NULL AND (up.expiresAt IS NULL OR up.expiresAt > CURRENT_TIMESTAMP)"
    )
    Optional<UserPermissionEntity> findActiveAssignment(@Param("userId") Long userId, @Param("permissionId") Long permissionId);
}
