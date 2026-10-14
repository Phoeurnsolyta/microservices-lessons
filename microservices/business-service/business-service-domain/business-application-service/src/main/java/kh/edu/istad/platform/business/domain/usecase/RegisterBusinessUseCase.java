package kh.edu.istad.platform.business.domain.usecase;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessCommand;
import kh.edu.istad.platform.business.domain.dto.RegisterBusinessResult;
import kh.edu.istad.platform.business.domain.entity.Business;
import kh.edu.istad.platform.business.domain.port.out.BusinessRepository;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class RegisterBusinessUseCase {

    private final BusinessRepository businessRepository;

    public RegisterBusinessUseCase(
            BusinessRepository businessRepository
    ) {
        this.businessRepository = businessRepository;
    }

    public Mono<RegisterBusinessResult> execute(
            RegisterBusinessCommand command
    ) {
        return Mono.defer(() -> {
            Business business = Business.register(
                    new BusinessId(UUID.randomUUID()),
                    new BusinessName(
                            command.legalName(),
                            command.displayName()
                    ),
                    new BusinessContact(
                            command.emails(),
                            command.phoneNumber()
                    )
            );

            return businessRepository.save(business)
                    .map(savedBusiness -> new RegisterBusinessResult(
                            savedBusiness.getBusinessId().value(),
                            savedBusiness.getBusinessName().legalName(),
                            savedBusiness.getBusinessName().displayName(),
                            savedBusiness.getBusinessContact().emails(),
                            savedBusiness.getBusinessContact().phoneNumber(),
                            savedBusiness.isActive()
                    ));
        });
    }

}
