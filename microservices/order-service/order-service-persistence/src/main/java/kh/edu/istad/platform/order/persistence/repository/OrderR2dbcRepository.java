package kh.edu.istad.platform.order.persistence.repository;

import kh.edu.istad.platform.order.persistence.entity.OrderEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface OrderR2dbcRepository extends ReactiveCrudRepository<OrderEntity, UUID> {
}
