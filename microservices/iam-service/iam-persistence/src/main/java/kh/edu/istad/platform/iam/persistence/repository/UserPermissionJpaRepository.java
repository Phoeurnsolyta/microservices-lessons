package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.UserPermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPermissionJpaRepository extends JpaRepository<UserPermissionEntity, Long> {
}
