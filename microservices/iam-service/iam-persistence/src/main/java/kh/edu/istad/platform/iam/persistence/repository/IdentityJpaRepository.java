package kh.edu.istad.platform.iam.persistence.repository;

import kh.edu.istad.platform.iam.persistence.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityJpaRepository extends JpaRepository<IdentityEntity, Long> {
}
