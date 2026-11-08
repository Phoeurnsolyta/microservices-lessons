package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, Long> {

    List<UserRoleEntity> findActiveByUserId(Long userId);

    Optional<UserRoleEntity> findActiveAssignment(Long userId, Long roleId);
}
