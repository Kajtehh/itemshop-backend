package pl.kajteh.itemshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kajteh.itemshop.model.order.Order;
import pl.kajteh.itemshop.repository.OrderRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderService implements CommonService<Order, UUID> {

    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return this.orderRepository.save(order);
    }

    @Override
    public void delete(UUID id) {
        this.orderRepository.deleteById(id);
    }

    @Override
    public Optional<Order> get(UUID id) {
        return this.orderRepository.findById(id);
    }

    public List<Order> getAll() {
        return this.orderRepository.findAll();
    }
}
