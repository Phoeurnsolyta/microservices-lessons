package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.event.BusinessRegisteredEvent;
import kh.edu.istad.platform.business.domain.event.OrderApprovalEvent;
import kh.edu.istad.platform.business.domain.event.OrderApprovedEvent;
import kh.edu.istad.platform.business.domain.event.OrderRejectedEvent;
import kh.edu.istad.platform.business.domain.exception.BusinessDomainException;
import kh.edu.istad.platform.business.domain.valueobject.BusinessContact;
import kh.edu.istad.platform.business.domain.valueobject.BusinessName;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalStatus;
import kh.edu.istad.platform.business.domain.valueobject.OrderStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Business {

    private final BusinessId businessId;
    private final BusinessName businessName;
    private final BusinessContact businessContact;
    private boolean active;
    private BusinessRegisteredEvent businessRegisteredEvent;
    private OrderApproval orderApproval;
    private OrderDetail orderDetail;

    private final List<DomainEvent<?>> domainEvents = new ArrayList<>();

    private Business(
            BusinessId businessId,
            BusinessName businessName,
            BusinessContact businessContact
    ) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.businessContact = businessContact;
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

        business.domainEvents.add(business.businessRegisteredEvent);

        return business;
    }

//    validateBusiness()
    public void validateBusiness() {
        if (!active) {
            throw new BusinessDomainException(
                    "Inactive businesses cannot process orders");
        }
    }
// initializeBusiness()
    public void initializeBusiness() {
        this.active = true;
    }

    public void initializeOrder(OrderDetail orderDetail) {
        if (orderDetail == null) {
            throw new BusinessDomainException(
                    "Order detail cannot be null");
        }

        this.orderDetail = orderDetail;
        this.orderApproval = null;
    }
//validateOrder()
    public void validateOrder() {
        validateBusiness();

        if (orderDetail == null) {
            throw new BusinessDomainException(
                    "Order detail must be initialized first");
        }
        if (orderDetail.getOrderStatus() != OrderStatus.PENDING
                && orderDetail.getOrderStatus() != OrderStatus.PAID) {
            throw new BusinessDomainException(
                    "Only pending or paid orders can be reviewed");
        }
        if (orderDetail.getProducts().isEmpty()) {
            throw new BusinessDomainException(
                    "Order must contain at least one product");
        }
        for (Product product : orderDetail.getProducts()) {
            if (!product.isAvailable()) {
                throw new BusinessDomainException(
                        "Product is unavailable: " + product.getName());
            }

            if (product.getQuantity() <= 0) {
                throw new BusinessDomainException(
                        "Product quantity must be greater than zero: "
                                + product.getName());
            }
        }

        if (orderDetail.calculateTotal().amount().signum() <= 0) {
            throw new BusinessDomainException(
                    "Order total must be greater than zero");
        }
    }

//    constructOrderApproval()
    public OrderApproval constructOrderApproval() {
        if (orderDetail == null) {
            throw new BusinessDomainException(
                    "Order detail must be initialized before approval");
        }

        List<String> failureMessages = new ArrayList<>();

        try {
            validateOrder();
        } catch (IllegalStateException exception) {
            failureMessages.add(exception.getMessage());
        }

        OrderApprovalStatus status = failureMessages.isEmpty()
                ? OrderApprovalStatus.APPROVED
                : OrderApprovalStatus.REJECTED;

        this.orderApproval = OrderApproval.create(
                businessId,
                orderDetail.getOrderId(),
                status
        );

        ZonedDateTime now = ZonedDateTime.now();

        domainEvents.add(new OrderApprovalEvent(
                orderApproval,
                businessId,
                failureMessages,
                now
        ));

        if (status == OrderApprovalStatus.APPROVED) {
            domainEvents.add(new OrderApprovedEvent(
                    orderApproval,
                    this,
                    failureMessages,
                    now
            ));
        } else {
            domainEvents.add(new OrderRejectedEvent(
                    orderApproval,
                    businessId,
                    failureMessages,
                    now
            ));
        }

        return orderApproval;
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
