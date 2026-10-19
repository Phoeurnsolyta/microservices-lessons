package kh.edu.istad.platform.customer.persistence.repository;

import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CustomerMongoRepository extends ReactiveMongoRepository<CustomerDocument, UUID> {

    @Query ("{username: {$eq: ?0}}")
    Mono<CustomerDocument> findByUsername(String username);
}
