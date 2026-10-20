package kh.edu.istad.platform.customer.restapi.controller;

import jakarta.validation.Valid;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatisticResult;
import kh.edu.istad.platform.customer.domain.dto.InitiateCustomerResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.usecase.*;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.restapi.dto.*;
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
    private final GetCustomerByIdUseCase getCustomerByIdUseCase;
    private final DeleteCustomerUseCase deleteCustomerUseCase;
    private final AssignCustomerSegmentUseCase assignCustomerSegmentUseCase;
    private final CustomerStatisticUseCase customerStatisticUseCase;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Customer> create (@RequestBody CreateCustomerRequest request) {
        return createCustomerUseCase.create(customerWebMapper.toDomain(request));
    }

    @GetMapping("/{id}")
    public Mono<Customer> findById(@PathVariable("id") UUID id) {
        return getCustomerByIdUseCase.execute(id);
    }

    @PutMapping("/{id}")
    public Mono<Customer> updateCustomer(
            @PathVariable("id") UUID id,
            @Valid @RequestBody CustomerUpdateRequest request
    ) {
        return updateCustomerUseCase.execute(
                id,
                request.familyName(),
                request.givenName()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable("id") UUID id) {
        return deleteCustomerUseCase.execute(id);
    }


    @GetMapping ("/query")
    public Flux<Customer> query(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "status", required = false) CustomerStatus status
    ) {
        return queryCustomerUseCase.query(username, email, status);
    }

    @GetMapping
    public Flux<Customer> findAll() {
        return queryCustomerUseCase.findAll();
    }

    @PutMapping("/{customerId}/segment")
    public Mono<Customer> assignSegmentToCustomer(
            @PathVariable("customerId") UUID customerId,
            @RequestBody AssignCustomerSegmentRequest request
    ) {
        return assignCustomerSegmentUseCase.execute(
                customerId,
                request.segmentId()
        );
    }

    @GetMapping("/statistic")
    public Mono<CustomerStatisticResult> getCustomerStatistics() {
        return customerStatisticUseCase.execute();
    }

}
