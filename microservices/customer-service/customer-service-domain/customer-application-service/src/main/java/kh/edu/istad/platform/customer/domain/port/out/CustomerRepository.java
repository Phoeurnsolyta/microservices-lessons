package kh.edu.istad.platform.customer.domain.port.out;


import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatisticResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {
    Mono <Customer> save(Customer customer);

    Mono<Customer> findById(CustomerId id);

    Flux<Customer> findAll();

    Mono<Void> deleteById(CustomerId id);

    Mono<Customer> findByUsername(String username);

    Flux<Customer> query(String username, String email, CustomerStatus status);

    Mono<Customer> assignSegmentToCustomer(UUID customerId, UUID segmentId);

    Flux<CustomerStatisticResult> getCustomerStatistics();
}
