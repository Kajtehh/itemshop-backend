package pl.kajteh.itemshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kajteh.itemshop.filter.RequireApiKey;
import pl.kajteh.itemshop.model.Product;
import pl.kajteh.itemshop.service.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/{version}/products")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ProductController {

    private final ProductService productService;

    @GetMapping("/server/{serverId}")
    public List<Product> getProducts(@PathVariable UUID serverId) {
        return this.productService.getAll(serverId);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable UUID productId) {
        final Optional<Product> optionalProduct = this.productService.get(productId);

        return optionalProduct.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequireApiKey
    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        return this.productService.save(product);
    }

    @RequireApiKey
    @DeleteMapping("/{productId}")
    public void deleteProduct(@PathVariable UUID productId) {
        this.productService.delete(productId);
    }
}
