package pl.kajteh.itemshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kajteh.itemshop.model.Variant;
import pl.kajteh.itemshop.repository.VariantRepository;
import pl.kajteh.itemshop.service.VariantService;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;

    @Override
    public Optional<Variant> getVariant(UUID id) {
        return this.variantRepository.findById(id);
    }
}
