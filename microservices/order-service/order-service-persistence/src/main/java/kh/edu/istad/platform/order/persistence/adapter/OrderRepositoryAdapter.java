package kh.edu.istad.platform.order.persistence.adapter;

//import jakarta.transaction.Transactional;
import kh.edu.istad.common.domain.valueobject.CustomerId;
import kh.edu.istad.common.domain.valueobject.OrderId;
import kh.edu.istad.platform.order.domain.entity.Order;
import kh.edu.istad.platform.order.domain.entity.OrderItem;
import kh.edu.istad.platform.order.domain.entity.Product;
import kh.edu.istad.platform.order.domain.port.out.OrderRepository;
import kh.edu.istad.platform.order.domain.valueobject.*;
import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import kh.edu.istad.platform.order.persistence.repository.OrderItemJpaRepository;
import kh.edu.istad.platform.order.persistence.repository.OrderJpaRepository;
import kh.edu.istad.platform.order.domain.valueobject.OrderItemId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderItemJpaRepository orderItemJpaRepository;

    @Override
//    @Transactional
    public Order save(Order order) {
        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setId(order.getId().value());
        orderEntity.setCustomerId(order.getCustomerId().value());
        orderEntity.setBusinessId(order.getBusinessId().value());
        orderEntity.setStreet(order.getDeliveryAddress().street());
        orderEntity.setPostalCode(order.getDeliveryAddress().postalCode());
        orderEntity.setCity(order.getDeliveryAddress().city());
        orderEntity.setPrice(order.getPrice().amount());
        orderEntity.setTrackingId(order.getTrackingId().value());
        orderEntity.setOrderStatus(order.getOrderStatus());
        orderEntity.setCreatedAt(order.getCreatedAt());

        OrderEntity savedOrder = orderJpaRepository.save(orderEntity);

        List<OrderItemEntity> itemEntities = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            OrderItemEntity itemEntity = new OrderItemEntity();

            itemEntity.setId(item.getId().value());
            itemEntity.setOrderId(savedOrder.getId());
            itemEntity.setProductId(item.getProduct().productId());
            itemEntity.setProductName(item.getProduct().name());
            itemEntity.setQuantity(item.getQuantity());
            itemEntity.setPrice(item.getPrice().amount());
            itemEntity.setSubTotal(item.getSubTotal().amount());

            itemEntities.add(itemEntity);
        }

        List<OrderItemEntity> savedItems =
                orderItemJpaRepository.saveAll(itemEntities);

        List<OrderItem> domainItems = savedItems.stream()
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
                .id(new OrderId(savedOrder.getId()))
                .customerId(new CustomerId(savedOrder.getCustomerId()))
                .businessId(new BusinessId(savedOrder.getBusinessId()))
                .deliveryAddress(new StreetAddress(
                        savedOrder.getStreet(),
                        savedOrder.getPostalCode(),
                        savedOrder.getCity()
                ))
                .price(new Money(savedOrder.getPrice()))
                .items(domainItems)
                .trackingId(new TrackingId(savedOrder.getTrackingId()))
                .orderStatus(savedOrder.getOrderStatus())
                .createdAt(savedOrder.getCreatedAt())
                .build();
    }
}
