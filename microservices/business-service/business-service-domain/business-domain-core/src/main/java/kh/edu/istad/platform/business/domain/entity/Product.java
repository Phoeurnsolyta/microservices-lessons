package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.platform.business.domain.valueobject.Money;
import kh.edu.istad.platform.business.domain.valueobject.ProductId;

import java.util.Objects;

public class Product {

    private final ProductId productId;
    private String name;
    private Money price;
    private int quantity;
    private boolean available;

    public Product(
            ProductId productId,
            String name,
            Money price,
            int quantity,
            boolean available
    ) {
        this.productId = Objects.requireNonNull(
                productId, "Product ID cannot be null");

        setName(name);
        setPrice(price);
        setQuantity(quantity);
        this.available = available;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Product name cannot be blank");
        }
        this.name = name.trim();
    }

    public void setPrice(Money price) {
        this.price = Objects.requireNonNull(
                price, "Product price cannot be null");
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(
                    "Product quantity cannot be negative");
        }
        this.quantity = quantity;
    }

    public void markAvailable() {
        this.available = true;
    }

    public void markUnavailable() {
        this.available = false;
    }

    public ProductId getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return available;
    }
}
