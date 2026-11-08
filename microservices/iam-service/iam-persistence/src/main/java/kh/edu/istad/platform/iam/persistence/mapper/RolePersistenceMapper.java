package kh.edu.istad.platform.iam.persistence.mapper;

import kh.edu.istad.platform.iam.domain.dto.RoleSummary;
import kh.edu.istad.platform.iam.domain.entity.Role;
import kh.edu.istad.platform.iam.domain.entity.RolePermission;
import kh.edu.istad.platform.iam.persistence.entity.RoleEntity;
import kh.edu.istad.platform.iam.persistence.entity.RolePermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RolePersistenceMapper {

    Role toDomain(RoleEntity entity);

    RoleEntity toEntity(Role domain);

    @Mapping(target = "active", source = "active")
    RoleSummary toSummary(RoleEntity entity);

    RolePermission toDomain(RolePermissionEntity entity);

    RolePermissionEntity toEntity(RolePermission domain);
}
