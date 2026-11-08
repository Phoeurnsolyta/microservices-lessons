package kh.edu.istad.platform.iam.persistence.adapter;

import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.UserFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.UserSummary;
import kh.edu.istad.platform.iam.domain.entity.Identity;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserPermission;
import kh.edu.istad.platform.iam.domain.entity.UserRole;
import kh.edu.istad.platform.iam.domain.port.out.UserRepository;
import kh.edu.istad.platform.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.platform.iam.persistence.entity.UserEntity;
import kh.edu.istad.platform.iam.persistence.mapper.UserPersistenceMapper;
import kh.edu.istad.platform.iam.persistence.repository.IdentityJpaRepository;
import kh.edu.istad.platform.iam.persistence.repository.UserJpaRepository;
import kh.edu.istad.platform.iam.persistence.repository.UserPermissionJpaRepository;
import kh.edu.istad.platform.iam.persistence.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final IdentityJpaRepository identityJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;
    private final UserPermissionJpaRepository userPermissionJpaRepository;
    private final UserPersistenceMapper userPersistenceMapper;

    @Override
    public User save(User user) {
        UserEntity entity = userPersistenceMapper.toEntity(user);
        return userPersistenceMapper.toDomain(userJpaRepository.save(entity));
    }

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUuid(UUID uuid) {
        return userJpaRepository.findByUuid(uuid).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userJpaRepository.findByUsername(username).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        return userJpaRepository.findByUsernameOrEmail(usernameOrEmail).map(userPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public PageResult<UserSummary> findAll(UserFilterQuery query) {
        Sort sort = Sort.by(Sort.Direction.ASC, "username");

        if (query.sort() != null && !query.sort().isBlank()) {
            String[] parts = query.sort().split(",");
            String property = parts[0].trim();

            Sort.Direction direction =
                    parts.length > 1 && parts[1].trim().equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sort = Sort.by(direction, property);
        }

        Pageable pageable = PageRequest.of(query.page(), query.size(), sort);

        Page<UserEntity> page = userJpaRepository
                .searchUsers(
                        query.search(),
                        query.enabled(), pageable
                );

        List<UserSummary> content = page.getContent().stream()
                .map(userPersistenceMapper::toSummary)
                .toList();

        return new PageResult<>(
                content,
                new PageResult.PageMetadata(
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages())
        );
    }

    @Override
    public List<UserRole> findActiveRolesByUserId(Long userId) {
        return userRoleJpaRepository.findActiveByUserId(userId).stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<UserPermission> findActivePermissionsByUserId(Long userId) {
        return userPermissionJpaRepository.findActiveByUserId(userId).stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<UserRole> findActiveUserRole(Long userId, Long roleId) {
        return userRoleJpaRepository.findActiveAssignment(userId, roleId)
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<UserPermission> findActiveUserPermission(Long userId, Long permissionId) {
        return userPermissionJpaRepository.findActiveAssignment(userId, permissionId)
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    public void saveUserRole(UserRole userRole) {
        userRoleJpaRepository.save(userPersistenceMapper.toEntity(userRole));
    }

    @Override
    public void saveUserPermission(UserPermission userPermission) {
        userPermissionJpaRepository.save(userPersistenceMapper.toEntity(userPermission));
    }

    @Override
    public Identity saveIdentity(Identity identity) {
        return userPersistenceMapper
                .toDomain(identityJpaRepository.save(userPersistenceMapper.toEntity(identity)));
    }

    @Override
    public List<Identity> findIdentitiesByUserId(Long userId) {
        return identityJpaRepository.findByUserIdAndIsDeletedFalse(userId).stream()
                .map(userPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Identity> findIdentityByUuid(UUID identityUuid) {
        return identityJpaRepository.findByUuid(identityUuid)
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Identity> findIdentityByProvider(
            IdentityProvider provider, String providerSubject) {
        return identityJpaRepository
                .findByProviderAndProviderSubjectAndIsDeletedFalse(
                        provider.name(), providerSubject
                )
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByUserIdAndProvider(Long userId, IdentityProvider provider) {
        return identityJpaRepository
                .existsByUserIdAndProviderAndIsDeletedFalse(userId, provider.name());
    }
}
