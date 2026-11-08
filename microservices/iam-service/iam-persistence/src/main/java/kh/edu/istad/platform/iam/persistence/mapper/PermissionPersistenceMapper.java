package kh.edu.istad.platform.iam.persistence.mapper;

import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.domain.entity.Permission;
import kh.edu.istad.platform.iam.persistence.entity.PermissionEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PermissionPersistenceMapper {

    Permission toDomain(PermissionEntity entity);

    PermissionEntity toEntity(Permission domain);

    @Mapping(target = "active", source = "active")
    PermissionResult toResult(PermissionEntity entity);
}
