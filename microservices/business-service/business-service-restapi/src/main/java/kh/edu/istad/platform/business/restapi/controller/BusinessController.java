package kh.edu.istad.platform.business.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.business.domain.usecase.RegisterBusinessUseCase;
import kh.edu.istad.platform.business.restapi.dto.BusinessRegisterRequest;
import kh.edu.istad.platform.business.restapi.dto.BusinessRegisterResponse;
import kh.edu.istad.platform.business.restapi.mapper.BusinessWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final RegisterBusinessUseCase registerBusinessUseCase;
    private final BusinessWebMapper businessWebMapper;

    public BusinessController(
            RegisterBusinessUseCase registerBusinessUseCase,
            BusinessWebMapper businessWebMapper
    ) {
        this.registerBusinessUseCase = registerBusinessUseCase;
        this.businessWebMapper = businessWebMapper;
    }

    @PostMapping
    public Mono<ResponseEntity<BusinessRegisterResponse>> registerBusiness(
            @Valid @RequestBody Mono<BusinessRegisterRequest> request
    ) {
        return request
                .map(businessWebMapper::toCommand)
                .flatMap(registerBusinessUseCase::execute)
                .map(businessWebMapper::toResponse)
                .map(response ->
                        ResponseEntity.status(HttpStatus.CREATED).body(response)
                );
    }
}
