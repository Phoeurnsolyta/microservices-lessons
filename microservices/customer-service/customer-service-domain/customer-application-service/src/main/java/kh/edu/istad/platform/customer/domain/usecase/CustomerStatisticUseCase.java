package kh.edu.istad.platform.customer.domain.usecase;

import kh.edu.istad.platform.customer.domain.dto.CustomerStatisticResult;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class CustomerStatisticUseCase {

    private final CustomerRepository customerRepository;

    public Flux<CustomerStatisticResult> execute() {
        return customerRepository.getCustomerStatistics();
    }
}
