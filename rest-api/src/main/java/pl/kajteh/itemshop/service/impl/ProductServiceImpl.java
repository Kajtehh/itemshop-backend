package pl.kajteh.itemshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kajteh.itemshop.model.Product;
import pl.kajteh.itemshop.repository.ProductRepository;
import pl.kajteh.itemshop.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Optional<Product> getProduct(UUID id) {
        return this.productRepository.findById(id);
    }

    @Override
    public List<Product> getProductsByServer(UUID serverId) {
        return this.productRepository.findAllByServerId(serverId);
    }

    @Override
    public Product saveProduct(Product product) {
        return this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        this.productRepository.deleteById(id);
    }
}
