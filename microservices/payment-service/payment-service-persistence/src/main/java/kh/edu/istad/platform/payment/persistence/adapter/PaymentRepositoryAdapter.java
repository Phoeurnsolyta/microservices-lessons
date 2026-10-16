package kh.edu.istad.platform.payment.persistence.adapter;

import kh.edu.istad.platform.payment.application.port.out.PaymentRepository;
import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.persistence.entity.PaymentEntity;
import kh.edu.istad.platform.payment.persistence.repository.PaymentJpaRepository;
import org.springframework.stereotype.Component;

@Component
public class PaymentRepositoryAdapter implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    public PaymentRepositoryAdapter( PaymentJpaRepository paymentJpaRepository ) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Payment save (Payment payment) {
        PaymentEntity entity = new PaymentEntity(
                payment.getPaymentId().value(),
                payment.getOrderId().value(),
                payment.getCustomerId().value(),
                payment.getPrice().amount(),
                payment.getPaymentStatus(),
                payment.getCreatedAt()
        );

        paymentJpaRepository.save(entity);

        return payment;
    }
}
