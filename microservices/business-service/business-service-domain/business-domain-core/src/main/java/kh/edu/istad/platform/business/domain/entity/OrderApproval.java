package kh.edu.istad.platform.business.domain.entity;

import kh.edu.istad.common.domain.valueobject.BusinessId;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalId;
import kh.edu.istad.platform.business.domain.valueobject.OrderApprovalStatus;

import java.util.Objects;

public class OrderApproval {

    private final OrderApprovalId orderApprovalId;
    private final BusinessId businessId;
    private final OrderId orderId;
    private OrderApprovalStatus approvalStatus;

    public OrderApproval(
            OrderApprovalId orderApprovalId,
            BusinessId businessId,
            OrderId orderId,
            OrderApprovalStatus approvalStatus
    ) {
        this.orderApprovalId = Objects.requireNonNull(
                orderApprovalId, "Order approval ID cannot be null");
        this.businessId = Objects.requireNonNull(
                businessId, "Business ID cannot be null");
        this.orderId = Objects.requireNonNull(
                orderId, "Order ID cannot be null");
        this.approvalStatus = Objects.requireNonNull(
                approvalStatus, "Approval status cannot be null");
    }

    public static OrderApproval create(
            BusinessId businessId,
            OrderId orderId,
            OrderApprovalStatus approvalStatus
    ) {
        return new OrderApproval(
                OrderApprovalId.generate(),
                businessId,
                orderId,
                approvalStatus
        );
    }

    public void approve() {
        this.approvalStatus = OrderApprovalStatus.APPROVED;
    }

    public void reject() {
        this.approvalStatus = OrderApprovalStatus.REJECTED;
    }

    public OrderApprovalId getOrderApprovalId() {
        return orderApprovalId;
    }

    public BusinessId getBusinessId() {
        return businessId;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public OrderApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

}
