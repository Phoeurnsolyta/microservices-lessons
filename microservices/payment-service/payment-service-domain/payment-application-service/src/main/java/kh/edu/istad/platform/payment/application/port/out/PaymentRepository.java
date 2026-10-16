package kh.edu.istad.platform.payment.application.port.out;

import kh.edu.istad.platform.payment.domain.entity.Payment;

public interface PaymentRepository {

    Payment save(Payment payment);
}
