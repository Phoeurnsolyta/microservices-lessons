package kh.edu.istad.platform.payment.application.port.out;

import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;

import java.util.Optional;

public interface CreditEntryRepository {

    Optional<CreditEntry> findByCustomerId(CustomerId customerId);

    CreditEntry save(CreditEntry creditEntry);
}
