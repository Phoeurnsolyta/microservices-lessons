package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.payment.application.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.valueobject.CreditEntryId;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.domain.valueobject.Money;
import kh.edu.istad.platform.payment.persistence.entity.CreditEntryEntity;
import kh.edu.istad.platform.payment.persistence.repository.CreditEntryJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CreditEntryRepositoryAdapter implements CreditEntryRepository {

    private final CreditEntryJpaRepository creditEntryJpaRepository;

    public CreditEntryRepositoryAdapter(CreditEntryJpaRepository creditEntryJpaRepository) {
        this.creditEntryJpaRepository = creditEntryJpaRepository;
    }

    @Override
    public Optional<CreditEntry> findByCustomerId(CustomerId customerId) {
        return creditEntryJpaRepository
                .findByCustomerId(customerId.value())
                .map(this::toDomain);
    }

    @Override
    public CreditEntry save(CreditEntry creditEntry) {
        CreditEntryEntity entity = new CreditEntryEntity(
                creditEntry.getCreditEntryId().value(),
                creditEntry.getCustomerId().value(),
                creditEntry.getTotalCreditAmount().amount()
        );

        creditEntryJpaRepository.save(entity);

        return creditEntry;
    }

    private CreditEntry toDomain(CreditEntryEntity entity) {
        return CreditEntry.restore(
                new CreditEntryId(entity.getCreditEntryId()),
                new CustomerId(entity.getCustomerId()),
                new Money(entity.getTotalCreditAmount())
        );
    }

}
