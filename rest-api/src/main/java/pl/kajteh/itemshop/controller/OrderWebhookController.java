package pl.kajteh.itemshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kajteh.itemshop.model.Variant;
import pl.kajteh.itemshop.model.order.Order;
import pl.kajteh.itemshop.model.order.OrderStatus;
import pl.kajteh.itemshop.security.RequireApiKey;
import pl.kajteh.itemshop.service.OrderService;
import pl.kajteh.itemshop.service.VariantService;
import pl.kajteh.payment.CashBillPaymentDetails;
import pl.kajteh.payment.CashBillPaymentException;
import pl.kajteh.payment.CashBillPaymentProcessor;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/{version}/orders/webhook")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderWebhookController {

    private final CashBillPaymentProcessor paymentProcessor;
    private final OrderService orderService;
    private final VariantService variantService;

    @GetMapping("/payment/status")
    public ResponseEntity<String> handlePaymentUpdate(
            @RequestParam String cmd,
            @RequestParam String args,
            @RequestParam String sign) {
        try {
            final CashBillPaymentDetails payment = this.paymentProcessor.processPaymentStatusChange(cmd, sign, args);

            final Optional<Order> optionalOrder = this.orderService.getOrder(UUID.fromString(payment.getAdditionalData()));

            if (optionalOrder.isEmpty()) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            final Order order = optionalOrder.get();

            order.setStatus(switch (payment.getStatus()) {
                case "PositiveFinish" -> OrderStatus.PAYED;
                case "NegativeAuthorization", "Abort", "Fraud", "NegativeFinish" -> OrderStatus.FAILED;
                default -> OrderStatus.PROCESSING;
            });

            this.orderService.saveOrder(order);

            return ResponseEntity.ok("OK");
        } catch (CashBillPaymentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @RequireApiKey
    @PostMapping("/receive/{orderId}")
    public ResponseEntity<?> receiveOrder(@PathVariable UUID orderId) {
        final Optional<Order> optionalOrder = this.orderService.getOrder(orderId);

        if(optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final Order order = optionalOrder.get();

        if(order.getStatus() != OrderStatus.PAYED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Order isn't payed");
        }

        final Variant variant = this.variantService.getVariant(order.getVariantId()).orElse(null);

        if(variant == null) {
            return ResponseEntity.internalServerError().body("Variant was not found");
        }

        order.setStatus(OrderStatus.RECEIVED);

        return ResponseEntity.ok(variant.getCommands()); // TODO
    }
}
