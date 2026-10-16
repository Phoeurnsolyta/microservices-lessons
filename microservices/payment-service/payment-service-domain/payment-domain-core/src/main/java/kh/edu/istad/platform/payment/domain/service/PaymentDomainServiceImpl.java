package kh.edu.istad.platform.payment.domain.service;

import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.valueobject.PaymentStatus;
import kh.edu.istad.platform.payment.domain.valueobject.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class PaymentDomainServiceImpl implements PaymentDomainService {
    @Override
    public List<String> validatePayment(Payment payment, CreditEntry creditEntry) {

        List<String> failures = new ArrayList<>();

        if (!payment.getCustomerId().equals(creditEntry.getCustomerId())) {
            failures.add(
                    "Payment customer does not match credit account customer"
            );
        }

        if (payment.getPrice().isGreaterThan(
                creditEntry.getTotalCreditAmount()
        )) {
            failures.add("Insufficient customer credit balance");
        }

        return List.copyOf(failures);
    }

    @Override
    public CreditHistory completePayment(Payment payment, CreditEntry creditEntry) {

        List<String> failures = validatePayment(payment, creditEntry);

        if (!failures.isEmpty()) {
            throw new PaymentDomainException(
                    String.join("; ", failures)
            );
        }

        creditEntry.subtractCreditAmount(payment.getPrice());
        payment.updateStatus(PaymentStatus.COMPLETED);

        return CreditHistory.create(
                payment.getCustomerId(),
                payment.getPrice(),
                TransactionType.DEBIT
        );
    }
}
