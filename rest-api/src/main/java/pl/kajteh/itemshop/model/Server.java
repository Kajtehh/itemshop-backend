package pl.kajteh.itemshop.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String image;
    private String name;

    @OneToMany(mappedBy = "server", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Product> products;

    public Server(String name) {
        this.name = name;
    }
}
