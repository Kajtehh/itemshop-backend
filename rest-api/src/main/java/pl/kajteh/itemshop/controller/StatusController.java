package pl.kajteh.itemshop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/{version}/status")
public class StatusController {

    @GetMapping
    public ResponseEntity<String> getStatus() {
        boolean allSystemsOperational = this.checkSystems();

        return allSystemsOperational
                ? ResponseEntity.ok("All systems are operational")
                : ResponseEntity.internalServerError().body("Some systems are down");
    }

    private boolean checkSystems() {
        return true; // TODO
    }
}
