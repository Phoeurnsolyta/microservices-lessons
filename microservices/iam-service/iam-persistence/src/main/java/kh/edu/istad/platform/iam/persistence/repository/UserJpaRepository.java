package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUuid(UUID uuid);

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    @Query("SELECT u FROM UserEntity u " +
            "WHERE (LOWER(u.username) = LOWER(:identifier) OR LOWER(u.email) " +
            "= LOWER(:identifier)) AND u.isDeleted = false")
    Optional<UserEntity> findByUsernameOrEmail(@Param("identifier") String identifier);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<UserEntity> findByIsDeletedFalse(Pageable pageable);

    Page<UserEntity> findByEnabledAndIsDeletedFalse(boolean enabled, Pageable pageable);

    @Query("""
        SELECT u FROM UserEntity u
        WHERE (LOWER(u.username) LIKE :searchPattern
               OR LOWER(u.email) LIKE :searchPattern
               OR LOWER(u.givenName) LIKE :searchPattern
               OR LOWER(u.familyName) LIKE :searchPattern)
          AND (:enabled IS NULL OR u.enabled = :enabled)
          AND u.isDeleted = false
    """)
    Page<UserEntity> searchUsers(
            @Param("searchPattern") String searchPattern,
            @Param("enabled") Boolean enabled,
            Pageable pageable
    );
}