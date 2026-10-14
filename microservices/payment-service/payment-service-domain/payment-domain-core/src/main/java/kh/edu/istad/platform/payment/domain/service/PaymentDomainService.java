package kh.edu.istad.platform.payment.domain.service;

import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.entity.Payment;

import java.util.List;

public interface PaymentDomainService {

    List<String> validatePayment( Payment payment, CreditEntry creditEntry);

    CreditHistory completePayment( Payment payment, CreditEntry creditEntry);
}
