package pl.kajteh.itemshop.service;

import java.util.Optional;

public interface CommonService<T, ID> {
    T save(T t);
    void delete(ID id);
    Optional<T> get(ID id);
}
