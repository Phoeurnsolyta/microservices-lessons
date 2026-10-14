package kh.edu.istad.platform.payment.application.service;

import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.valueobject.CustomerId;
import kh.edu.istad.platform.payment.domain.valueobject.Money;
import kh.edu.istad.platform.payment.domain.valueobject.OrderId;

public interface PaymentApplicationService {

    Payment createPayment(
            OrderId orderId,
            CustomerId customerId,
            Money price
    );
}
