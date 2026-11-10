package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.PermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {

    Optional<PermissionEntity> findByUuid(UUID uuid);

    Optional<PermissionEntity> findByName(String name);

    boolean existsByName(String name);

    Page<PermissionEntity> findByIsDeletedFalse(Pageable pageable);

    Page<PermissionEntity> findByResourceAndIsDeletedFalse(String resource, Pageable pageable);

    @Query("""
                SELECT p FROM PermissionEntity p
                WHERE (:resource IS NULL OR p.resource = :resource)
                  AND (LOWER(p.name) LIKE :searchPattern OR LOWER(p.description) LIKE :searchPattern)
                  AND p.isDeleted = false
            """)
    Page<PermissionEntity> searchPermissions(
            @Param("resource") String resource,
            @Param("searchPattern") String searchPattern,
            Pageable pageable
    );
}