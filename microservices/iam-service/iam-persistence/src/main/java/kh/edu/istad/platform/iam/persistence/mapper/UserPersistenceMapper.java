package kh.edu.istad.platform.iam.persistence.mapper;

import kh.edu.istad.platform.iam.domain.dto.UserSummary;
import kh.edu.istad.platform.iam.domain.entity.Identity;
import kh.edu.istad.platform.iam.domain.entity.User;
import kh.edu.istad.platform.iam.domain.entity.UserPermission;
import kh.edu.istad.platform.iam.domain.entity.UserRole;
import kh.edu.istad.platform.iam.domain.valueobject.IdentityProvider;
import kh.edu.istad.platform.iam.domain.valueobject.PermissionEffect;
import kh.edu.istad.platform.iam.persistence.entity.IdentityEntity;
import kh.edu.istad.platform.iam.persistence.entity.UserEntity;
import kh.edu.istad.platform.iam.persistence.entity.UserPermissionEntity;
import kh.edu.istad.platform.iam.persistence.entity.UserRoleEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    User toDomain(UserEntity entity);

    UserEntity toEntity(User domain);

    UserSummary toSummary(UserEntity entity);

    Identity toDomain(IdentityEntity entity);


    IdentityEntity toEntity(Identity domain);

    UserRole toDomain(UserRoleEntity entity);

    UserRoleEntity toEntity(UserRole domain);

    UserPermission toDomain(UserPermissionEntity entity);

    UserPermissionEntity toEntity(UserPermission domain);

    default IdentityProvider stringToProvider(String provider) {
        return provider != null ? IdentityProvider.valueOf(provider.toUpperCase()) : null;
    }

    default PermissionEffect stringToEffect(String effect) {
        return effect != null ? PermissionEffect.valueOf(effect.toUpperCase()) : null;
    }
}
