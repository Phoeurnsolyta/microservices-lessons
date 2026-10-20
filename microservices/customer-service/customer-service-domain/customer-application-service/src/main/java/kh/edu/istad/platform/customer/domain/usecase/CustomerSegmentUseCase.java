package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.entity.CustomerSegment;
import kh.edu.istad.platform.customer.domain.port.out.CustomerSegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomerSegmentUseCase {

    private final CustomerSegmentRepository customerSegmentRepository;

    public Mono<CustomerSegment> create(CustomerSegment customerSegment) {
        CustomerSegment newSegment = new CustomerSegment(
                UUID.randomUUID(),
                customerSegment.getName(),
                customerSegment.getDescription()
        );

        return customerSegmentRepository.save(newSegment);
    }

    public Mono<CustomerSegment> findById(UUID id) {
        return customerSegmentRepository.findById(id);
    }

    public Flux<CustomerSegment> findAll() {
        return customerSegmentRepository.findAll();
    }

    public Mono<CustomerSegment> update(
            UUID id,
            String name,
            String description
    ) {
        return customerSegmentRepository.findById(id)
                .map(customerSegment -> {
                    customerSegment.update(name, description);
                    return customerSegment;
                })
                .flatMap(customerSegmentRepository::save);
    }

    public Mono<Void> deleteById(UUID id) {
        return customerSegmentRepository.deleteById(id);
    }
}
