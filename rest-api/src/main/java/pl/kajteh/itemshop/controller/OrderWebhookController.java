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
import pl.kajteh.payment.CashBillPaymentException;
import pl.kajteh.payment.CashBillPaymentUtil;
import pl.kajteh.payment.CashBillShop;
import pl.kajteh.payment.data.CashBillPaymentDetails;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/{version}/orders/webhook")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderWebhookController {

    private final CashBillShop shop;
    private final OrderService orderService;
    private final VariantService variantService;

    @GetMapping("/payment/status")
    public ResponseEntity<String> handlePaymentUpdate(
            @RequestParam String cmd,
            @RequestParam String args,
            @RequestParam String sign) {
        final String correctSignature = CashBillPaymentUtil.generatePaymentNotificationSignature(cmd, args, this.shop.getSecretKey());

        if(!sign.equals(correctSignature) || !cmd.equals("transactionStatusChanged")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature or command");
        }

        try {
            final CashBillPaymentDetails payment = this.shop.getPayment(args);

            final Optional<Order> optionalOrder = this.orderService.get(UUID.fromString(payment.getAdditionalData()));

            if(optionalOrder.isEmpty()) {
                return new ResponseEntity<>("Order not found", HttpStatus.NOT_FOUND);
            }

            final Order order = optionalOrder.get();

            order.setStatus(switch (payment.getStatus()) {
                case "PositiveFinish" -> OrderStatus.PAYED;
                case "NegativeAuthorization", "Abort", "Fraud", "NegativeFinish" -> OrderStatus.FAILED;
                default -> OrderStatus.PROCESSING;
            });

            this.orderService.save(order);

            return ResponseEntity.ok("OK");
        } catch (CashBillPaymentException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequireApiKey
    @PostMapping("/receive/{orderId}")
    public ResponseEntity<?> receiveOrder(@PathVariable UUID orderId) {
        final Optional<Order> optionalOrder = this.orderService.get(orderId);

        if(optionalOrder.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        final Order order = optionalOrder.get();

        if(order.getStatus() != OrderStatus.PAYED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Order isn't payed");
        }

        final Variant variant = this.variantService.get(order.getVariantId()).orElse(null);

        if(variant == null) {
            return ResponseEntity.internalServerError().body("Variant was not found");
        }

        order.setStatus(OrderStatus.RECEIVED);

        return ResponseEntity.ok(variant.getCommands()); // TODO
    }
}
