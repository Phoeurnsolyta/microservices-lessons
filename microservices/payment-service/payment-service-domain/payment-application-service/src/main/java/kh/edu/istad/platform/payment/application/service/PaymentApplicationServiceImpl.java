package kh.edu.istad.platform.payment.application.service;

import kh.edu.istad.platform.payment.application.port.out.CreditEntryRepository;
import kh.edu.istad.platform.payment.application.port.out.CreditHistoryRepository;
import kh.edu.istad.platform.payment.application.port.out.PaymentRepository;
import kh.edu.istad.platform.payment.domain.entity.CreditEntry;
import kh.edu.istad.platform.payment.domain.entity.CreditHistory;
import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.service.PaymentDomainService;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.domain.valueobject.Money;
import kh.edu.istad.platform.payment.domain.valueobject.OrderId;
import kh.edu.istad.platform.payment.domain.valueobject.PaymentStatus;

import java.util.List;
import java.util.Optional;

public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    private final PaymentRepository paymentRepository;
    private final CreditEntryRepository creditEntryRepository;
    private final CreditHistoryRepository creditHistoryRepository;
    private final PaymentDomainService paymentDomainService;

    public PaymentApplicationServiceImpl(
            PaymentRepository paymentRepository,
            CreditEntryRepository creditEntryRepository,
            CreditHistoryRepository creditHistoryRepository,
            PaymentDomainService paymentDomainService
    ) {
        this.paymentRepository = paymentRepository;
        this.creditEntryRepository = creditEntryRepository;
        this.creditHistoryRepository = creditHistoryRepository;
        this.paymentDomainService = paymentDomainService;
    }

    @Override
    public Payment createPayment(
            OrderId orderId,
            CustomerId customerId,
            Money price
    ) {
        Payment payment = Payment.initializePayment(
                orderId,
                customerId,
                price
        );

        Optional<CreditEntry> creditEntryOptional =
                creditEntryRepository.findByCustomerId(customerId);

        if (creditEntryOptional.isEmpty()) {
            payment.updateStatus(
                    PaymentStatus.FAILED
            );

            return paymentRepository.save(payment);
        }

        CreditEntry creditEntry = creditEntryOptional.get();

        List<String> failures = paymentDomainService.validatePayment(
                payment,
                creditEntry
        );

        if (!failures.isEmpty()) {
            payment.updateStatus(PaymentStatus.FAILED);
            return paymentRepository.save(payment);
        }

        CreditHistory creditHistory =
                paymentDomainService.completePayment(payment, creditEntry);

        creditEntryRepository.save(creditEntry);
        creditHistoryRepository.save(creditHistory);

        return paymentRepository.save(payment);

    }
}
