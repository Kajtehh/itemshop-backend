package pl.kajteh.itemshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kajteh.itemshop.model.Product;

import java.util.List;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByServerId(UUID serverId);
}
