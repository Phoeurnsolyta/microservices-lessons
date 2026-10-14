package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.business.domain.valueobject.Money;
import kh.edu.istad.platform.business.domain.valueobject.OrderStatus;

import java.util.List;
import java.util.Objects;

public class OrderDetail {

    private final OrderId orderId;
    private OrderStatus orderStatus;
    private Money totalAmount;
    private final List<Product> products;

    public OrderDetail(
            OrderId orderId,
            OrderStatus orderStatus,
            Money totalAmount,
            List<Product> products
    ) {
        this.orderId = Objects.requireNonNull(
                orderId, "Order ID cannot be null");
        this.orderStatus = Objects.requireNonNull(
                orderStatus, "Order status cannot be null");
        this.products = List.copyOf(
                Objects.requireNonNull(products, "Products cannot be null"));

        if (this.products.isEmpty()) {
            throw new IllegalArgumentException(
                    "Order must contain at least one product");
        }

        this.totalAmount = calculateTotal();
        validateTotalAmount(totalAmount);
    }

    public Money calculateTotal() {
        return products.stream()
                .map(product -> new Money(
                        product.getPrice().amount()
                                .multiply(
                                        java.math.BigDecimal.valueOf(
                                                product.getQuantity()
                                        )
                                )
                ))
                .reduce(Money.zero(), Money::add);
    }

    private void validateTotalAmount(Money suppliedTotal) {
        Objects.requireNonNull(
                suppliedTotal, "Total amount cannot be null");

        if (calculateTotal().amount()
                .compareTo(suppliedTotal.amount()) != 0) {
            throw new IllegalArgumentException(
                    "Supplied total does not match product totals");
        }
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = Objects.requireNonNull(
                orderStatus, "Order status cannot be null");
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Money getTotalAmount() {
        return totalAmount;
    }

    public List<Product> getProducts() {
        return products;
    }
}
