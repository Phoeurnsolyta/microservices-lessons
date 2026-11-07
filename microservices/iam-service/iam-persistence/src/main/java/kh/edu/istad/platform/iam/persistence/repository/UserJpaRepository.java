package kh.edu.istad.platform.iam.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<kh.edu.istad.platform.iam.persistence.enitity.UserEntity, Long> {

    boolean existsByUsername(String username);

    Optional<kh.edu.istad.platform.iam.persistence.enitity.UserEntity> findByUsernameOrEmail (String username, String email);
}
