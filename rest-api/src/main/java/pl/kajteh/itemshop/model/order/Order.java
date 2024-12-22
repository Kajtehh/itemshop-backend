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
    private UUID id;

    private String cashBillId;
    private String cashBillLink;

    private int quantity;
    private String nickname;
    private String email;
    private String paymentChannel;
    private OrderStatus status;

    private double totalPrice;

    private UUID serverId;
    private UUID productId;
    private UUID variantId;

    @UpdateTimestamp
    private Instant updatedAt;

    @CreationTimestamp
    private Instant createdAt;

    public Order(UUID id) {
        this.id = id;
    }
}
