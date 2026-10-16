package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.payment.application.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.persistence.entity.CreditHistoryEntity;
import kh.edu.istad.platform.payment.persistence.repository.CreditHistoryJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class CreditHistoryRepositoryAdapter implements CreditHistoryRepository {

    private final CreditHistoryJpaRepository creditHistoryJpaRepository;

    public CreditHistoryRepositoryAdapter(CreditHistoryJpaRepository creditHistoryJpaRepository) {
        this.creditHistoryJpaRepository = creditHistoryJpaRepository;
    }

    @Override
    public CreditHistory save(CreditHistory creditHistory) {
        CreditHistoryEntity entity = new CreditHistoryEntity(
                creditHistory.getCreditHistoryId().value(),
                creditHistory.getCustomerId().value(),
                creditHistory.getAmount().amount(),
                creditHistory.getTransactionType()
        );

        creditHistoryJpaRepository.save(entity);

        return creditHistory;
    }
}
