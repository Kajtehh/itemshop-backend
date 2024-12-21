package pl.kajteh.itemshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kajteh.itemshop.model.Server;

import java.util.UUID;

public interface ServerRepository extends JpaRepository<Server, UUID> {
}
