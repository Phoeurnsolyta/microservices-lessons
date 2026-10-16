package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.valueobject.Money;
import kh.edu.istad.platform.business.domain.valueobject.ProductId;

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
        this.productId = productId;

        setName(name);
        setPrice(price);
        setQuantity(quantity);
        this.available = available;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessDomainException(
                    "Product name cannot be blank");
        }
        this.name = name.trim();
    }

    public void setPrice(Money price) {
        if (price == null) {
            throw new BusinessDomainException(
                    "Product price cannot be null");
        }

        this.price = price;
    }

    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new BusinessDomainException(
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
