package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {

    @Query("SELECT ur FROM UserRoleEntity ur " +
            "WHERE ur.userId = :userId " +
            "AND ur.revokedAt " +
            "IS NULL AND (ur.expiresAt IS NULL OR ur.expiresAt > CURRENT_TIMESTAMP)"
    )
    List<UserRoleEntity> findActiveByUserId(@Param("userId") Long userId);

    @Query("SELECT ur FROM UserRoleEntity ur " +
            "WHERE ur.userId = :userId " +
            "AND ur.roleId = :roleId AND ur.revokedAt " +
            "IS NULL AND (ur.expiresAt IS NULL OR ur.expiresAt > CURRENT_TIMESTAMP)"
    )
    Optional<UserRoleEntity> findActiveAssignment(@Param("userId") Long userId, @Param("roleId") Long roleId);
}
