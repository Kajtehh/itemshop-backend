package pl.kajteh.itemshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kajteh.itemshop.model.Order;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
