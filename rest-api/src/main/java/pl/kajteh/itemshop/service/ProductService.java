package pl.kajteh.itemshop.service;

import pl.kajteh.itemshop.model.Product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    Optional<Product> getProduct(UUID id);
    List<Product> getProductsByServer(UUID serverId);
    Product saveProduct(Product product);
    void deleteProduct(UUID id);
}
