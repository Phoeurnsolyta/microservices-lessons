package kh.edu.istad.platform.payment.domain.entity;

import kh.edu.istad.platform.payment.domain.valueobject.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class Payment {

    private final PaymentId paymentId;
    private final OrderId orderId;
    private final CustomerId customerId;
    private final Money price;
    private PaymentStatus paymentStatus;
    private final ZonedDateTime createdAt;

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
        Objects.requireNonNull(orderId, "Order ID is required");
        Objects.requireNonNull(customerId, "Customer ID is required");
        Objects.requireNonNull(price, "Payment price is required");

        if (price.amount().signum() <= 0) {
            throw new IllegalArgumentException(
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
        Objects.requireNonNull(newStatus, "Payment status is required");

        if (paymentStatus != PaymentStatus.PENDING) {
            throw new IllegalStateException(
                    "Only a pending payment can change status"
            );
        }

        if (newStatus == PaymentStatus.PENDING) {
            throw new IllegalArgumentException(
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
