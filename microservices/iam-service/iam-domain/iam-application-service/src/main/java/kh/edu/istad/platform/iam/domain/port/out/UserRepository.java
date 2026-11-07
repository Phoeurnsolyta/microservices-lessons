package kh.edu.istad.platform.iam.domain.port.out;

import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.UserFilterQuery;
import kh.edu.istad.platform.iam.domain.dto.UserSummary;
import kh.edu.istad.platform.iam.domain.entity.Identity;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserPermission;
import kh.edu.istad.platform.iam.domain.entity.UserRole;
import kh.edu.istad.platform.iam.domain.valueobject.IdentityProvider;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    PageResult<UserSummary> findAll(UserFilterQuery query);

    List<UserRole> findActiveRolesByUserId(Long userId);

    List<UserPermission> findActivePermissionsByUserId(Long userId);

    Optional<UserRole> findActiveUserRole(Long userId, Long roleId);

    Optional<UserPermission> findActiveUserPermission(Long userId, Long permissionId);

    void saveUserRole(UserRole userRole);

    void saveUserPermission(UserPermission userPermission);

    Identity saveIdentity(Identity identity);

    List<Identity> findIdentitiesByUserId(Long userId);

    Optional<Identity> findIdentityByUuid(UUID identityUuid);

    Optional<Identity> findIdentityByProvider(IdentityProvider provider, String providerSubject);

    boolean existsByUserIdAndProvider(Long userId, IdentityProvider provider);
}
