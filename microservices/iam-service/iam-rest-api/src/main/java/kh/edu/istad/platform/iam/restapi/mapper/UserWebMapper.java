package kh.edu.istad.platform.iam.restapi.mapper;

import kh.edu.istad.platform.iam.domain.dto.*;
import kh.edu.istad.platform.iam.restapi.dto.request.AddressRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.CreateUserRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.RegisterUserRequest;
import kh.edu.istad.platform.iam.restapi.dto.request.UpdateUserProfileRequest;
import kh.edu.istad.platform.iam.restapi.dto.response.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserWebMapper {

    RegisterUserCommand toCommand(RegisterUserRequest request);

    @Mapping(target = "address", source = "address")
    CreateUserCommand toCommand(CreateUserRequest request);

    AddressCommand toCommand(AddressRequest request);

    @Mapping(target = "userUuid", source = "userUuid")
    @Mapping(target = "address", source = "request.address")
    UpdateUserProfileCommand toCommand(UUID userUuid, UpdateUserProfileRequest request);

    UserResponse toResponse(UserResult result);

    UserRoleSummaryResponse toRoleSummaryResponse(RoleSummary summary);

    UserAuthoritiesResponse toAuthoritiesResponse(UserAuthoritiesResult result);

    IdentityResponse toIdentityResponse(IdentityResult result);

    PageResponse<UserSummary> toPageResponse(PageResult<UserSummary> pageResult);
}
