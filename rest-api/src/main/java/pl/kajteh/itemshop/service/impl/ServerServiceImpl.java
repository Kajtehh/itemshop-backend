package pl.kajteh.itemshop.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kajteh.itemshop.model.Server;
import pl.kajteh.itemshop.repository.ServerRepository;
import pl.kajteh.itemshop.service.ServerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    @Override
    public Optional<Server> getServer(UUID id) {
        return this.serverRepository.findById(id);
    }

    @Override
    public List<Server> getServers() {
        return this.serverRepository.findAll();
    }

    @Override
    public Server saveServer(Server server) {
        return this.serverRepository.save(server);
    }

    @Override
    public void deleteServer(UUID id) {
        this.serverRepository.deleteById(id);
    }
}
