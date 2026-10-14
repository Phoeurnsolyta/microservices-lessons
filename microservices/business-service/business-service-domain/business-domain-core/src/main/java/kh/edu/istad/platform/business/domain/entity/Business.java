package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;

import java.time.ZonedDateTime;
import java.util.Objects;

public class Business {

    private final BusinessId businessId;
    private final BusinessName businessName;
    private final BusinessContact businessContact;
    private boolean active;
    private BusinessRegisteredEvent businessRegisteredEvent;
    private OrderApproval orderApproval;
    private OrderDetail orderDetail;

    private Business(
            BusinessId businessId,
            BusinessName businessName,
            BusinessContact businessContact
    ) {
        this.businessId = Objects.requireNonNull(
                businessId, "Business ID cannot be null");
        this.businessName = Objects.requireNonNull(
                businessName, "Business name cannot be null");
        this.businessContact = Objects.requireNonNull(
                businessContact, "Business contact cannot be null");
        this.active = true;
    }

    public static Business register(
            BusinessId businessId,
            BusinessName businessName,
            BusinessContact businessContact
    ) {
        Business business = new Business(
                businessId,
                businessName,
                businessContact
        );

        business.businessRegisteredEvent = new BusinessRegisteredEvent(
                business,
                ZonedDateTime.now()
        );

        return business;
    }

    public void deactivate() {
        this.active = false;
    }

    public BusinessRegisteredEvent getBusinessRegisteredEvent() {
        return businessRegisteredEvent;
    }

    public BusinessId getBusinessId() {
        return businessId;
    }

    public BusinessName getBusinessName() {
        return businessName;
    }

    public BusinessContact getBusinessContact() {
        return businessContact;
    }

    public boolean isActive() {
        return active;
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }
}
