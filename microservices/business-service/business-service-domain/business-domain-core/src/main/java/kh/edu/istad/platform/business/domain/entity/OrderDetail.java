package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.valueobject.Money;
import kh.edu.istad.platform.business.domain.valueobject.OrderStatus;

import java.util.List;

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
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.products = List.copyOf(products);

        if (this.products.isEmpty()) {
            throw new BusinessDomainException(
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
        if (suppliedTotal == null) {
            throw new BusinessDomainException(
                    "Total amount cannot be null");
        }

        if (calculateTotal().amount()
                .compareTo(suppliedTotal.amount()) != 0) {
            throw new BusinessDomainException(
                    "Supplied total does not match product totals");
        }
    }

    public void updateStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
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
