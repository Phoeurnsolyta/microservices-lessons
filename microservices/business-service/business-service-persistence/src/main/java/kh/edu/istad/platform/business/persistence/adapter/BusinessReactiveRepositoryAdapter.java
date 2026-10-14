package kh.edu.istad.platform.business.persistence.adapter;

import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.business.persistence.document.BusinessDocument;
import kh.edu.istad.platform.business.persistence.repository.BusinessMongoRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class BusinessReactiveRepositoryAdapter implements BusinessRepository {

    private final BusinessMongoRepository mongoRepository;

    public BusinessReactiveRepositoryAdapter(
            BusinessMongoRepository mongoRepository
    ) {
        this.mongoRepository = mongoRepository;
    }

    @Override
    public Mono<Business> save(Business business) {

        BusinessDocument document = new BusinessDocument(
                business.getBusinessId().value(),
                business.getBusinessName().legalName(),
                business.getBusinessName().displayName(),
                business.getBusinessContact().email(),
                business.getBusinessContact().phoneNumber(),
                business.isActive()
        );

        return mongoRepository.save(document)
                .thenReturn(business);
    }

}
