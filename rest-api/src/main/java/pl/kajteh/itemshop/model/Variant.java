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
public class Variant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;
    private double price;

    @ElementCollection
    @CollectionTable(
            name = "variant_commands",
            joinColumns = @JoinColumn(name = "variant_id")
    )
    @Column(name = "command")
    private List<String> commands;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    public Variant(String name, double price, List<String> commands, Product product) {
        this.name = name;
        this.price = price;
        this.commands = commands;
        this.product = product;
    }
}
