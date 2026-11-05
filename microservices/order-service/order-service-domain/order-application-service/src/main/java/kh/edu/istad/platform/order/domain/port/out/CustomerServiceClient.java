package kh.edu.istad.platform.order.domain.port.out;

import kh.edu.istad.platform.order.domain.dto.QueryCustomerResult;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerServiceClient {

    record CustomerResponse(
            String customerId,
            String username,
            String familyName,
            String givenName,
            String email,
            String phoneNumber,
            String customerStatus,
            String customerSegmentType
    ) {}

    record CustomerDetails(
            String customerId,
            String username,
            String familyName,
            String givenName,
            String email,
            String phoneNumber,
            String status,
            String segmentId
    ) {}

    Flux<QueryCustomerResult> getCustomerName();
    Flux<CustomerResponse> getCustomers();
    Mono<CustomerDetails> getCustomerById(String customerId);
}
