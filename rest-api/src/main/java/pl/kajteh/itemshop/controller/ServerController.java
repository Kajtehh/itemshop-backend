package pl.kajteh.itemshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kajteh.itemshop.filter.RequireApiKey;
import pl.kajteh.itemshop.model.Server;
import pl.kajteh.itemshop.service.ServerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/{version}/servers")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ServerController {

    private final ServerService serverService;

    @GetMapping
    public List<Server> getServers() {
        return this.serverService.getAll();
    }

    @GetMapping("/{serverId}")
    public ResponseEntity<Server> getServer(@PathVariable UUID serverId) {
        final Optional<Server> optionalServer = this.serverService.get(serverId);

        return optionalServer.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @RequireApiKey
    @PostMapping
    public Server saveServer(@RequestBody Server server) {
        return this.serverService.save(server);
    }

    @RequireApiKey
    @DeleteMapping("/{serverId}")
    public ResponseEntity<String> deleteServer(@PathVariable UUID serverId) {
        if(this.serverService.get(serverId).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Server was not found.");
        }

        this.serverService.delete(serverId);

        return ResponseEntity.ok("Server successfully deleted.");
    }
}
