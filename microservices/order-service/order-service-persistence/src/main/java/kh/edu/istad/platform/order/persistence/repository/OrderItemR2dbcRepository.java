package kh.edu.istad.platform.order.persistence.repository;

import kh.edu.istad.platform.order.persistence.entity.OrderItemEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemR2dbcRepository extends ReactiveCrudRepository<OrderItemEntity, UUID> {

    List<OrderItemEntity> findAllByOrderId(UUID orderId);
}
