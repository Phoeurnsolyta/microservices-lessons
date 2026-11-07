package kh.edu.istad.platform.iam.restapi.mapper;

import kh.edu.istad.platform.iam.domain.dto.CreateRoleCommand;
import kh.edu.istad.platform.iam.domain.dto.PageResult;
import kh.edu.istad.platform.iam.domain.dto.RoleResult;
import kh.edu.istad.platform.iam.domain.dto.RoleSummary;
import kh.edu.istad.platform.iam.restapi.dto.request.CreateRoleRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.PageResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.RoleResponse;
import kh.edu.istad.platform.iam.restapi.dto.response.RoleSummaryResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleWebMapper {

    CreateRoleCommand toCommand(CreateRoleRequest request);

    RoleResponse toResponse(RoleResult result);

    RoleSummaryResponse toSummaryResponse(RoleSummary summary);

    PageResponse<RoleSummaryResponse> toPageResponse(PageResult<RoleSummary> pageResult);
}
