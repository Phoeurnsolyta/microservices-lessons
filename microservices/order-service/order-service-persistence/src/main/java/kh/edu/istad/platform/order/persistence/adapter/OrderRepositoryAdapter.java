
package kh.edu.istad.platform.order.persistence.adapter;

import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.valueobject.BusinessId;
import kh.edu.istad.platform.order.domain.valueobject.Money;
import kh.edu.istad.platform.order.domain.valueobject.OrderItemId;
import kh.edu.istad.platform.order.domain.valueobject.StreetAddress;
import kh.edu.istad.platform.order.domain.valueobject.TrackingId;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import kh.edu.istad.platform.order.persistence.repository.OrderItemR2dbcRepository;
import kh.edu.istad.platform.order.persistence.repository.OrderR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderR2dbcRepository orderRepository;
    private final OrderItemR2dbcRepository orderItemRepository;

    @Override
    @Transactional
    public Mono<Order> save(Order order) {
        OrderEntity entity = toEntity(order);

        return orderRepository.save(entity)
                .flatMap(savedOrder ->
                        saveItems(savedOrder, order.getItems())
                                .collectList()
                                .map(savedItems ->
                                        toDomain(savedOrder, savedItems)
                                )
                );
    }

    private Flux<OrderItemEntity> saveItems(
            OrderEntity savedOrder,
            List<OrderItem> items
    ) {
        List<OrderItemEntity> entities = items.stream()
                .map(item -> {
                    OrderItemEntity entity = new OrderItemEntity();
                    entity.setId(item.getId().value());
                    entity.setOrderId(savedOrder.getId());
                    entity.setProductId(item.getProduct().productId());
                    entity.setProductName(item.getProduct().name());
                    entity.setQuantity(item.getQuantity());
                    entity.setPrice(item.getPrice().amount());
                    entity.setSubTotal(item.getSubTotal().amount());
                    return entity;
                })
                .toList();

        return orderItemRepository.saveAll(entities);
    }

    private OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();

        entity.setId(order.getId().value());
        entity.setCustomerId(order.getCustomerId().value());
        entity.setBusinessId(order.getBusinessId().value());
        entity.setStreet(order.getDeliveryAddress().street());
        entity.setPostalCode(order.getDeliveryAddress().postalCode());
        entity.setCity(order.getDeliveryAddress().city());
        entity.setPrice(order.getPrice().amount());
        entity.setTrackingId(order.getTrackingId().value());
        entity.setOrderStatus(order.getOrderStatus());
        entity.setCreatedAt(order.getCreatedAt());

        return entity;
    }

    private Order toDomain(
            OrderEntity entity,
            List<OrderItemEntity> itemEntities
    ) {
        List<OrderItem> items = itemEntities.stream()
                .map(item -> OrderItem.builder()
                        .id(new OrderItemId(item.getId()))
                        .orderId(new OrderId(item.getOrderId()))
                        .product(new Product(
                                item.getProductId(),
                                item.getProductName()
                        ))
                        .quantity(item.getQuantity())
                        .price(new Money(item.getPrice()))
                        .build())
                .toList();

        return Order.builder()
                .id(new OrderId(entity.getId()))
                .customerId(new CustomerId(entity.getCustomerId()))
                .businessId(new BusinessId(entity.getBusinessId()))
                .deliveryAddress(new StreetAddress(
                        entity.getStreet(),
                        entity.getPostalCode(),
                        entity.getCity()
                ))
                .price(new Money(entity.getPrice()))
                .items(items)
                .trackingId(new TrackingId(entity.getTrackingId()))
                .orderStatus(entity.getOrderStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
