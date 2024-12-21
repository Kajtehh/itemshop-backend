package pl.kajteh.itemshop.service;

import java.util.List;
import java.util.Optional;

public interface CommonService<T, ID> {
    void save(T t);
    void delete(ID id);
    Optional<T> get(ID id);
    List<T> getAll();
}
