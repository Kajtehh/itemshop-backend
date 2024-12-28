package pl.kajteh.itemshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kajteh.itemshop.model.order.Order;
import pl.kajteh.itemshop.repository.OrderRepository;
import pl.kajteh.itemshop.service.OrderService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public Optional<Order> getOrder(UUID id) {
        return this.orderRepository.findById(id);
    }

    @Override
    public Order saveOrder(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public List<Order> getOrders() {
        return this.orderRepository.findAll();
    }
}
