package pl.kajteh.itemshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kajteh.itemshop.model.Variant;

import java.util.List;
import java.util.UUID;

public interface VariantRepository extends JpaRepository<Variant, UUID> {
    List<Variant> findProductVariants(UUID productId);

}
