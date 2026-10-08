package kh.edu.istad.platform.customer.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.platform.customer.domain.entity.Customer;

import java.time.ZonedDateTime;

public class CustomerDeactivatedEvent implements DomainEvent<Customer> {
    private final Customer customer;
    //    ZoneDateTime can change zone dateTime based on current country
    private final ZonedDateTime initiatedAt;

    public CustomerDeactivatedEvent(Customer customer, ZonedDateTime initiatedAt) {
        this.customer = customer;
        this.initiatedAt = initiatedAt;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ZonedDateTime getInitiatedAt() {
        return initiatedAt;
    }
}
