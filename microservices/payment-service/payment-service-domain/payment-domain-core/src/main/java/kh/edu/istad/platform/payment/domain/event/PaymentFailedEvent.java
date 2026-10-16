package kh.edu.istad.platform.payment.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.payment.domain.entity.Payment;
import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;

import java.time.ZonedDateTime;
import java.util.List;

public record PaymentFailedEvent(
        Payment payment,
        ZonedDateTime createdAt,
        List<String> failureMessages
) implements DomainEvent<Payment> {

    public PaymentFailedEvent {
        if (payment == null) {
            throw new PaymentDomainException("Payment is required");
        }

        if (createdAt == null) {
            throw new PaymentDomainException("Creation time is required");
        }

        failureMessages = failureMessages == null
                ? List.of()
                : List.copyOf(failureMessages);
    }
}
