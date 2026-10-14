package kh.edu.istad.platform.order.domain.service;

import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.event.OrderCancelledEvent;
import kh.edu.istad.platform.order.domain.event.OrderCreatedEvent;
import kh.edu.istad.platform.order.domain.event.OrderPaidEvent;

import java.time.ZonedDateTime;

public class OrderDomainServiceImpl implements OrderDomainService {
    @Override
    public OrderCreatedEvent createOrder(Order order) {
        order.initializeOrder();

        return new OrderCreatedEvent(order,order.getCreatedAt());
    }

    @Override
    public OrderPaidEvent payOrder(Order order) {
        order.pay();

        return new OrderPaidEvent(
                order,
                ZonedDateTime.now()
        );
    }

    @Override
    public OrderCancelledEvent cancelOrder(Order order) {
        order.initCancel();
        order.cancel();

        return new OrderCancelledEvent(
                order,
                ZonedDateTime.now()
        );
    }
}
