package kh.edu.istad.platform.payment.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.payment.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public record PaymentCancelledEvent(
        Payment payment,
        ZonedDateTime createdAt,
        List<String> failureMessages
) implements DomainEvent<Payment> {

    public PaymentCancelledEvent {
        Objects.requireNonNull(payment, "Payment is required");
        Objects.requireNonNull(createdAt, "Creation time is required");

        failureMessages = failureMessages == null
                ? List.of()
                : List.copyOf(failureMessages);
    }
}
