package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.PermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByUuid(UUID uuid);

    Optional<PermissionEntity> findByName(String name);

    boolean existsByName(String name);

    Page<PermissionEntity> searchPermissions(String resource, String search, Pageable pageable);
}
