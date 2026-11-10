package kh.edu.istad.platform.iam;

import kh.edu.istad.platform.iam.domain.entity.*;
import kh.edu.istad.platform.iam.domain.exception.InactiveAssignmentException;
import kh.edu.istad.platform.iam.domain.exception.LastAuthenticationMethodRemovalException;
import kh.edu.istad.platform.iam.domain.service.IamDomainService;
import kh.edu.istad.platform.iam.domain.service.IamDomainServiceImpl;
import kh.edu.istad.platform.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.platform.iam.domain.valueobject.PermissionEffect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IamDomainServiceTest {

    private IamDomainService domainService;
    private Instant now;

    @BeforeEach
    void setUp() {
        domainService = new IamDomainServiceImpl();
        now = Instant.now();
    }

    @Test
    @DisplayName("Effective permissions = Active Role Permissions + Direct GRANT")
    void computeEffectivePermissions_shouldCombineRolePermissionsAndDirectGrant() {
        Role userRole = Role.builder().id(1L).uuid(UUID.randomUUID()).name("USER").isActive(true).isDeleted(false).build();
        Permission userReadPerm = Permission.builder().id(10L).uuid(UUID.randomUUID()).name("user:read").isActive(true).isDeleted(false).build();

        UserRole ur = UserRole.builder().id(100L).userId(5L).roleId(1L).assignedAt(now).assignedBy("system").build();
        RolePermission rp = RolePermission.builder().id(200L).roleId(1L).permissionId(10L).assignedAt(now).assignedBy("system").build();

        Permission reportReadPerm = Permission.builder().id(20L).uuid(UUID.randomUUID()).name("report:read").isActive(true).isDeleted(false).build();
        UserPermission directGrant = UserPermission.builder()
                .id(300L).userId(5L).permissionId(20L).effect(PermissionEffect.GRANT)
                .assignedAt(now).assignedBy("system").build();

        Map<Long, Role> rolesById = Map.of(1L, userRole);
        Map<Long, Permission> permsById = Map.of(10L, userReadPerm, 20L, reportReadPerm);

        Set<String> effective = domainService.computeEffectivePermissions(
                now, List.of(ur), rolesById, List.of(rp), List.of(directGrant), permsById
        );

        assertThat(effective).containsExactlyInAnyOrder("user:read", "report:read");
    }

    @Test
    @DisplayName("Direct DENY overrides Role Permission (Effective = Role Perms - Direct DENY)")
    void computeEffectivePermissions_shouldSubtractDirectDeny() {
        Role adminRole = Role.builder().id(1L).uuid(UUID.randomUUID()).name("ADMIN").isActive(true).isDeleted(false).build();
        Permission userCreate = Permission.builder().id(10L).uuid(UUID.randomUUID()).name("user:create").isActive(true).isDeleted(false).build();
        Permission userDelete = Permission.builder().id(11L).uuid(UUID.randomUUID()).name("user:delete").isActive(true).isDeleted(false).build();

        UserRole ur = UserRole.builder().id(100L).userId(5L).roleId(1L).assignedAt(now).assignedBy("system").build();
        RolePermission rp1 = RolePermission.builder().id(201L).roleId(1L).permissionId(10L).assignedAt(now).assignedBy("system").build();
        RolePermission rp2 = RolePermission.builder().id(202L).roleId(1L).permissionId(11L).assignedAt(now).assignedBy("system").build();

        UserPermission directDeny = UserPermission.builder()
                .id(301L).userId(5L).permissionId(11L).effect(PermissionEffect.DENY)
                .assignedAt(now).assignedBy("system").build();

        Map<Long, Role> rolesById = Map.of(1L, adminRole);
        Map<Long, Permission> permsById = Map.of(10L, userCreate, 11L, userDelete);

        Set<String> effective = domainService.computeEffectivePermissions(
                now, List.of(ur), rolesById, List.of(rp1, rp2), List.of(directDeny), permsById
        );

        assertThat(effective).containsExactly("user:create");
        assertThat(effective).doesNotContain("user:delete");
    }

    @Test
    @DisplayName("Revoked or expired role permissions are excluded from effective permissions")
    void computeEffectivePermissions_shouldExcludeRevokedOrExpiredPermissions() {
        Role adminRole = Role.builder().id(1L).uuid(UUID.randomUUID()).name("ADMIN").isActive(true).isDeleted(false).build();
        Permission userRead = Permission.builder().id(10L).uuid(UUID.randomUUID()).name("user:read").isActive(true).isDeleted(false).build();

        UserRole ur = UserRole.builder().id(100L).userId(5L).roleId(1L).assignedAt(now).assignedBy("system").build();

        RolePermission revokedRp = RolePermission.builder()
                .id(201L).roleId(1L).permissionId(10L).assignedAt(now.minus(10, ChronoUnit.DAYS))
                .assignedBy("system").revokedAt(now.minus(1, ChronoUnit.DAYS)).revokedBy("admin").build();

        Map<Long, Role> rolesById = Map.of(1L, adminRole);
        Map<Long, Permission> permsById = Map.of(10L, userRead);

        Set<String> effective = domainService.computeEffectivePermissions(
                now, List.of(ur), rolesById, List.of(revokedRp), List.of(), permsById
        );

        assertThat(effective).isEmpty();
    }

    @Test
    @DisplayName("Assigning inactive role throws InactiveAssignmentException")
    void validateRoleAssignment_shouldThrowException_whenRoleIsInactive() {
        Role inactiveRole = Role.builder().id(2L).uuid(UUID.randomUUID()).name("OLD_ROLE").isActive(false).isDeleted(false).build();

        assertThrows(InactiveAssignmentException.class, () -> domainService.validateRoleAssignment(inactiveRole));
    }

    @Test
    @DisplayName("Unlinking last authentication method throws LastAuthenticationMethodRemovalException")
    void validateIdentityUnlink_shouldThrowException_whenUnlinkingLastAuthMethod() {
        User socialOnlyUser = User.builder().id(1L).uuid(UUID.randomUUID()).username("google_user").email("test@gmail.com")
                .passwordHash(null).build();

        Identity googleIdentity = Identity.builder().id(100L).uuid(UUID.randomUUID()).userId(1L)
                .provider(IdentityProvider.GOOGLE).providerSubject("12345").isDeleted(false).build();

        assertThrows(LastAuthenticationMethodRemovalException.class, () ->
                domainService.validateIdentityUnlink(socialOnlyUser, List.of(googleIdentity), 100L));
    }
}