package kh.edu.istad.platform.customer.restapi.controller;

import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.usecase.CustomerSegmentUseCase;
import kh.edu.istad.platform.customer.restapi.dto.CustomerSegmentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping ("/api/customer-segments")
public class CustomerSegmentController {

    private final CustomerSegmentUseCase customerSegmentUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<CustomerSegment> create(
            @RequestBody CustomerSegmentRequest request
    ) {
        CustomerSegment customerSegment = new CustomerSegment(
                null,
                request.name(),
                request.description()
        );

        return customerSegmentUseCase.create(customerSegment);
    }

    @GetMapping
    public Flux<CustomerSegment> findAll() {
        return customerSegmentUseCase.findAll();
    }

    @GetMapping("/{id}")
    public Mono<CustomerSegment> findById(
            @PathVariable("id") UUID id
    ) {
        return customerSegmentUseCase.findById(id);
    }

    @PutMapping("/{id}")
    public Mono<CustomerSegment> update(
            @PathVariable("id") UUID id,
            @RequestBody CustomerSegmentRequest request
    ) {
        return customerSegmentUseCase.update(
                id,
                request.name(),
                request.description()
        );
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(
            @PathVariable("id") UUID id
    ) {
        return customerSegmentUseCase.deleteById(id);
    }
}
