package pl.kajteh.itemshop.model.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.kajteh.itemshop.model.Product;
import pl.kajteh.itemshop.model.Server;
import pl.kajteh.itemshop.model.Variant;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "itemshop_order")
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private int quantity;
    private String nickname;
    private String email;
    private String paymentChannel;
    private OrderStatus status;

    @UpdateTimestamp
    private Instant updatedAt;

    @CreationTimestamp
    private Instant createdAt;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;
}
