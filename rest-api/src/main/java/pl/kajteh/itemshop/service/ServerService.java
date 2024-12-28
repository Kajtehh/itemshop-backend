package pl.kajteh.itemshop.service;

import pl.kajteh.itemshop.model.Server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerService {
    Optional<Server> getServer(UUID id);
    List<Server> getServers();
    Server saveServer(Server server);
    void deleteServer(UUID id);
}
