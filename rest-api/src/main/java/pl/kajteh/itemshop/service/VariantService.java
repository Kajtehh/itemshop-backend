package pl.kajteh.itemshop.service;

import pl.kajteh.itemshop.model.Variant;

import java.util.Optional;
import java.util.UUID;

public interface VariantService {
    Optional<Variant> getVariant(UUID id);
}
