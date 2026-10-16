package kh.edu.istad.platform.payment.application.port.out;

import kh.edu.istad.platform.payment.domain.entity.CreditHistory;

public interface CreditHistoryRepository {

    CreditHistory save(CreditHistory creditHistory);
}
