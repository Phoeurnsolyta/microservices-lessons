package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPermissionJpaRepository extends JpaRepository<UserPermissionEntity, Long> {

    List<UserPermissionEntity> findActiveByUserId(Long userId);

    Optional<UserPermissionEntity> findActiveAssignment(Long userId, Long permissionId);
}
