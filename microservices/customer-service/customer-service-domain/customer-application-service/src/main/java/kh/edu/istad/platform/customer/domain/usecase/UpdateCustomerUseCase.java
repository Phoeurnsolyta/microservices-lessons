package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.service.CustomerDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateCustomerUseCase {

    private final CustomerDomainService customerDomainService;
    private final CustomerRepository customerRepository;

    public Mono<Customer> execute(
            UUID customerId,
            String familyName,
            String givenName
    ) {
        CustomerId id = new CustomerId(customerId);

        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(
                        new CustomerDomainException(
                                "Customer id not found: " + customerId
                        )
                ))
                .flatMap(customer -> {
                    customerDomainService.updateCustomer(
                            customer,
                            familyName,
                            givenName
                    );

                    return customerRepository.save(customer);
                });
    }


}
