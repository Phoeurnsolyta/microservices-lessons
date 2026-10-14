package kh.edu.istad.platform.business.restapi.mapper;

import kh.edu.istad.platform.business.domain.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessResult;
import kh.edu.istad.platform.business.restapi.dto.BusinessRegisterRequest;
import kh.edu.istad.platform.business.restapi.dto.BusinessRegisterResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BusinessWebMapper {

    RegisterBusinessCommand toCommand(BusinessRegisterRequest request);

    BusinessRegisterResponse toResponse(RegisterBusinessResult result);
}
