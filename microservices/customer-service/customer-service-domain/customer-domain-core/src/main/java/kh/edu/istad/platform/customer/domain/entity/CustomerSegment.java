package kh.edu.istad.platform.customer.domain.entity;

import kh.edu.istad.platform.customer.domain.exception.CustomerDomainException;

import java.util.UUID;

public class CustomerSegment {

    private UUID id;
    private String name;
    private String description;

    public CustomerSegment(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void update(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
