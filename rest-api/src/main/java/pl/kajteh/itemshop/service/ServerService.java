package pl.kajteh.itemshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.kajteh.itemshop.model.Server;
import pl.kajteh.itemshop.repository.ServerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerService implements CommonService<Server, UUID> {

    private final ServerRepository serverRepository;

    @Override
    public Server save(Server server) {
        return this.serverRepository.save(server);
    }

    @Override
    public void delete(UUID id) {
        this.serverRepository.deleteById(id);
    }

    @Override
    public Optional<Server> get(UUID id) {
        return this.serverRepository.findById(id);
    }

    public List<Server> getAll() {
        return this.serverRepository.findAll();
    }
}
