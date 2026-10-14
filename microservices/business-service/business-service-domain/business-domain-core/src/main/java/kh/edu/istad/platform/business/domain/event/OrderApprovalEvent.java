package kh.edu.istad.platform.business.domain.event;

import kh.edu.istad.common.domain.event.DomainEvent;
import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.platform.business.domain.entity.OrderApproval;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;

public class OrderApprovalEvent implements DomainEvent<OrderApproval> {

    private final OrderApproval orderApproval;
    private final BusinessId businessId;
    private final List<String> failureMessages;
    private final ZonedDateTime createdAt;

    public OrderApprovalEvent(
    OrderApproval orderApproval,
    BusinessId businessId,
    List<String> failureMessages,
    ZonedDateTime createdAt
    ) {
        this.orderApproval = Objects.requireNonNull(
                orderApproval, "Order approval cannot be null");
        this.businessId = Objects.requireNonNull(
                businessId, "Business ID cannot be null");
        this.failureMessages = List.copyOf(
                Objects.requireNonNull(
                        failureMessages, "Failure messages cannot be null"));
        this.createdAt = Objects.requireNonNull(
                createdAt, "Creation time cannot be null");
    }

    public OrderApproval getOrderApproval() {
        return orderApproval;
    }

    public BusinessId getBusinessId() {
        return businessId;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
