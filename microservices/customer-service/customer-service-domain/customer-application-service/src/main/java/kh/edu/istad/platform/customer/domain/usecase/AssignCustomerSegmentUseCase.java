package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AssignCustomerSegmentUseCase {

    private final CustomerRepository customerRepository;

    public Mono<Customer> execute(UUID customerId, UUID segmentId) {
        return customerRepository.assignSegmentToCustomer(customerId, segmentId);
    }
}
