package kh.edu.istad.platform.order.domain.usecase;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.order.domain.dto.CreateOrderCommand;
import kh.edu.istad.platform.order.domain.dto.CreateOrderResult;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.service.OrderDomainService;
import kh.edu.istad.platform.order.domain.valueobject.Money;
import kh.edu.istad.platform.order.domain.valueobject.StreetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderDomainService orderDomainService;
    private final OrderRepository orderRepository;

    public CreateOrderResult execute(CreateOrderCommand command) {
        log.info("Create order use case: {}", command);

        OrderId orderId = new OrderId(UUID.randomUUID());

        List<OrderItem> items = command.items().stream()
                .map(item -> OrderItem.builder()
                        .id(new kh.edu.istad.platform.order.domain.valueobject.OrderItemId(
                                UUID.randomUUID()))
                        .orderId(orderId)
                        .product(new Product(
                                item.productId(),
                                item.productName()))
                        .quantity(item.quantity())
                        .price(new Money(item.price()))
                        .build())
                .toList();

        BigDecimal totalAmount = items.stream()
                .map(item -> item.getSubTotal().amount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .id(orderId)
                .customerId(new CustomerId(command.customerId()))
                .businessId(new kh.edu.istad.platform.order.domain.valueobject.BusinessId(
                        command.businessId()))
                .deliveryAddress(new StreetAddress(
                        command.street(),
                        command.postalCode(),
                        command.city()))
                .items(items)
                .price(new Money(totalAmount))
                .build();

        // Invoke domain logic.
        orderDomainService.createOrder(order);

        // Save through the output port.
        Order savedOrder = orderRepository.save(order);

        return new CreateOrderResult(
                savedOrder.getId().value(),
                savedOrder.getTrackingId().value());
    }

}
