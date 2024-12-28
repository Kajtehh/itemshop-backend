package pl.kajteh.itemshop.service;

import pl.kajteh.itemshop.model.order.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderService {
    Optional<Order> getOrder(UUID id);
    Order saveOrder(Order order);
    List<Order> getOrders();
}
