package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.usecase.*;
import kh.edu.istad.platform.customer.restapi.dto.CreateCustomerRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateRequest;
import kh.edu.istad.platform.customer.restapi.dto.CustomerInitiateResponse;
import kh.edu.istad.platform.customer.restapi.dto.CustomerUpdateRequest;
import kh.edu.istad.platform.customer.restapi.mapper.CustomerWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customers")
public class CustomerController {

    private final InitiateCustomerUseCase initiateCustomerUseCase;
    private final UpdateCustomerUseCase updateCustomerUseCase;
    private final DeactivateCustomerUseCase deactivateCustomerUseCase;
    private final CustomerWebMapper customerWebMapper;
    private final CreateCustomerUseCase  createCustomerUseCase;
    private final QueryCustomerUseCase queryCustomerUseCase;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> create (@RequestBody CreateCustomerRequest request) {
        return createCustomerUseCase.create(customerWebMapper.toDomain(request));
    }


//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public Mono<CustomerInitiateResponse> initiateCustomer(
//            @Valid @RequestBody CustomerInitiateRequest request
//    ) {
//        return initiateCustomerUseCase
//                .execute(customerWebMapper.toCommand(request))
//                .map(customerWebMapper::toResponse);
//    }

    @GetMapping ("/query")
    public Flux<Customer> query() {
        return queryCustomerUseCase.findAll();
    }

    @GetMapping
    public Flux<Customer> findAll() {
        return queryCustomerUseCase.findAll();
    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @PostMapping
//    public CustomerInitiateResponse initiateCustomer(
//            @RequestBody CustomerInitiateRequest customerInitiateRequest
//    ) {
//
//        InitiateCustomerResult result = initiateCustomerUseCase.execute(
//                customerWebMapper.toCommand(customerInitiateRequest)
//        );
//        return customerWebMapper.toResponse(result);
//    }
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PutMapping("/{id}")
//    public void updateCustomer(
//            @PathVariable("id") UUID customerId,
//            @Valid @RequestBody CustomerUpdateRequest customerUpdateRequest
//    ) {
//        updateCustomerUseCase.execute(
//                customerId,
//                customerUpdateRequest.familyName(),
//                customerUpdateRequest.givenName()
//        );
//    }
//
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PatchMapping("/{id}/deactivate")
//    public void deactivateCustomer(
//            @PathVariable ("id") UUID customerId) {
//        deactivateCustomerUseCase.execute(customerId);
//    }
}
