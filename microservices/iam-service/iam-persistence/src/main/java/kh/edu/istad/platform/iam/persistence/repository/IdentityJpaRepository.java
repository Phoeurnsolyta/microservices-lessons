package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IdentityJpaRepository extends JpaRepository<IdentityEntity, Long> {

    Optional<IdentityEntity> findByUuid(UUID uuid);

    List<IdentityEntity> findByUserIdAndIsDeletedFalse(Long userId);

    Optional<IdentityEntity> findByProviderAndProviderSubjectAndIsDeletedFalse(String provider, String providerSubject);

    boolean existsByUserIdAndProviderAndIsDeletedFalse(Long userId, String provider);
}
