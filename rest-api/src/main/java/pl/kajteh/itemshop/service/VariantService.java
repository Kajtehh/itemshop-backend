package pl.kajteh.itemshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import pl.kajteh.itemshop.model.Variant;
import pl.kajteh.itemshop.repository.VariantRepository;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VariantService implements CommonService<Variant, UUID> {

    private final VariantRepository variantRepository;

    @Override
    public Variant save(Variant variant) {
        return this.variantRepository.save(variant);
    }

    @Override
    public void delete(UUID id) {
        this.variantRepository.deleteById(id);
    }

    @Override
    public Optional<Variant> get(UUID id) {
        return this.variantRepository.findById(id);
    }
}
