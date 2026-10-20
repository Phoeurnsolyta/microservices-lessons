package kh.edu.istad.platform.customer.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.platform.customer.domain.dto.CustomerStatisticResult;
import kh.edu.istad.platform.customer.domain.entity.Customer;
import kh.edu.istad.platform.customer.domain.port.out.CustomerRepository;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatus;
import kh.edu.istad.platform.customer.domain.valueobject.CustomerStatusStatistic;
import kh.edu.istad.platform.customer.persistence.document.CustomerDocument;
import kh.edu.istad.platform.customer.persistence.mapper.CustomerPersistenceMapper;
import kh.edu.istad.platform.customer.persistence.repository.CustomerMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;


@RequiredArgsConstructor
@Repository
public class CustomerReactiveRepositoryAdapter implements CustomerRepository {

    private final CustomerMongoRepository customerMongoRepository;
    private final CustomerPersistenceMapper customerPersistenceMapper;
    private final ReactiveMongoTemplate reactiveMongoTemplate;


    @Override
    public Mono<Customer> save(Customer customer) {

        CustomerDocument customerDocument = customerPersistenceMapper.toDocument(customer);
        Mono<CustomerDocument> result = customerMongoRepository.save(customerDocument);

        return result
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Customer> findById(CustomerId id) {
        return customerMongoRepository.findById(id.value())
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Customer> findAll() {
        return customerMongoRepository.findAll()
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(CustomerId id) {
        return customerMongoRepository.deleteById(id.value());
    }

    @Override
    public Mono<Customer> findByUsername(String username) {
        return customerMongoRepository.findByUsername(username)
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Flux<Customer> query(String username, String email, CustomerStatus status) {

//        Criteria criteria = new Criteria("username");
        List<Criteria> criteriaList = new ArrayList<>();

        if (username != null && !username.isBlank()) {
            criteriaList.add(
                    Criteria.where("username")
                            .regex(Pattern.quote(username.trim()), "i")
            );
        }

        if (email != null && !email.isBlank()) {
            criteriaList.add(
                    Criteria.where("email")
                            .regex(Pattern.quote(email.trim()), "i")
            );
        }

        if (status != null) {
            criteriaList.add(
                    Criteria.where("status").is(status)
            );
        }

        Query query = new Query();

        if (!criteriaList.isEmpty()) {
            query.addCriteria(
                    new Criteria().andOperator(
                            criteriaList.toArray(new Criteria[0])
                    )
            );
        }
//        Aggregation
        return reactiveMongoTemplate.find(query, CustomerDocument.class)
                .map(customerPersistenceMapper::toDomain);
    }

    @Override
    public Mono<Customer> assignSegmentToCustomer(UUID customerId, UUID segmentId) {
        return customerMongoRepository.findById(customerId)
                .map(customerPersistenceMapper::toDomain)
                .map(customer -> {
                    customer.assignSegment(segmentId);
                    return customer;
                })
                .flatMap(this::save);
    }

    @Override
    public Mono<CustomerStatisticResult> getCustomerStatistics() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.group("status", "segmentId")
                        .count().as("total"),

                Aggregation.project("total")
                        .and("_id.status").as("status")
                        .and("_id.segmentId").as("segmentId")
        );
        Mono<Long> totalCustomers = reactiveMongoTemplate.count(
                new Query(),
                CustomerDocument.class
        );

        Mono<List<CustomerStatusStatistic>> statistics =
                reactiveMongoTemplate.aggregate(
                        aggregation,
                        CustomerDocument.class,
                        CustomerStatusStatistic.class
                ).collectList();

        return Mono.zip(totalCustomers, statistics)
                .map(result -> new CustomerStatisticResult(
                        result.getT1(),
                        result.getT2()
                ));
    }
}
