package kh.edu.istad.platform.iam.restapi.mapper;

import kh.edu.istad.platform.iam.domain.dto.CreatePermissionCommand;
import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.PermissionResult;
import kh.edu.istad.platform.iam.restapi.dto.request.CreatePermissionRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.PermissionResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionWebMapper {

    CreatePermissionCommand toCommand(CreatePermissionRequest request);

    PermissionResponse toResponse(PermissionResult result);

    PageResponse<PermissionResponse> toPageResponse(PageResult<PermissionResult> pageResult);
}
