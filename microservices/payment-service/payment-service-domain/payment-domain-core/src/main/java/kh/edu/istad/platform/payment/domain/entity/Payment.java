package kh.edu.istad.platform.payment.domain.entity;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.payment.domain.exception.PaymentDomainException;
import kh.edu.istad.platform.payment.domain.valueobject.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Payment {

    private final PaymentId paymentId;
    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money price;
    private PaymentStatus paymentStatus;
    private final ZonedDateTime createdAt;

    private final List<DomainEvent<Payment>> domainEvents =
            new ArrayList<>();

    public List<DomainEvent<Payment>> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    private Payment(
            PaymentId paymentId,
            OrderId orderId,
            CustomerId customerId,
            Money price,
            PaymentStatus paymentStatus,
            ZonedDateTime createdAt
    ) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.price = price;
        this.paymentStatus = paymentStatus;
        this.createdAt = createdAt;
    }

    public static Payment initializePayment(
            OrderId orderId,
            CustomerId customerId,
            Money price
    ) {
        if (orderId == null) {
            throw new PaymentDomainException("Order ID is required");
        }

        if (customerId == null) {
            throw new PaymentDomainException("Customer ID is required");
        }

        if (price == null) {
            throw new PaymentDomainException("Payment price is required");
        }

        if (price.amount().signum() <= 0) {
            throw new PaymentDomainException(
                    "Payment price must be greater than zero"
            );
        }

        return new Payment(
                new PaymentId(UUID.randomUUID()),
                orderId,
                customerId,
                price,
                PaymentStatus.PENDING,
                ZonedDateTime.now()
        );
    }

    public void updateStatus(PaymentStatus newStatus) {

        if (newStatus == null) {
            throw new PaymentDomainException(
                    "Payment status is required"
            );
        }

        if (paymentStatus != PaymentStatus.PENDING) {
            throw new PaymentDomainException(
                    "Only a pending payment can change status"
            );
        }

        if (newStatus == PaymentStatus.PENDING) {
            throw new PaymentDomainException(
                    "New status must be a final payment status"
            );
        }

        paymentStatus = newStatus;
    }

    public PaymentId getPaymentId() {
        return paymentId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public Money getPrice() {
        return price;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
