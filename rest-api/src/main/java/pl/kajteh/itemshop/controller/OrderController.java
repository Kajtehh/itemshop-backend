package pl.kajteh.itemshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.kajteh.itemshop.security.RequireApiKey;
import pl.kajteh.itemshop.model.Product;
import pl.kajteh.itemshop.model.Server;
import pl.kajteh.itemshop.model.Variant;
import pl.kajteh.itemshop.model.order.Order;
import pl.kajteh.itemshop.model.order.OrderRequest;
import pl.kajteh.itemshop.model.order.OrderStatus;
import pl.kajteh.itemshop.service.OrderService;
import pl.kajteh.itemshop.service.ServerService;
import pl.kajteh.itemshop.validator.EmailValidator;
import pl.kajteh.itemshop.validator.NicknameValidator;
import pl.kajteh.payment.CashBillPayment;
import pl.kajteh.payment.CashBillPaymentException;
import pl.kajteh.payment.CashBillPaymentProcessor;
import pl.kajteh.payment.data.CashBillAmountData;
import pl.kajteh.payment.data.CashBillGeneratedPayment;
import pl.kajteh.payment.data.CashBillPersonalData;

import java.util.List;
import java.util.UUID;

@RequireApiKey
@RestController
@RequestMapping("/api/{version}/orders")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderController {

    private final CashBillPaymentProcessor paymentProcessor;
    private final OrderService orderService;
    private final ServerService serverService;

    private static final String PAYMENT_DESCRIPTION = "Created with ❤️ by Kajteh";

    @Value("${shop.url}")
    private String shopUrl;

    @PostMapping("/start")
    public ResponseEntity<?> startOrder(@RequestBody OrderRequest orderRequest) {
        final String nickname = orderRequest.nickname();
        final String email = orderRequest.email();

        if(nickname == null || email == null
                || orderRequest.serverId() == null
                || orderRequest.productId() == null
                || orderRequest.variantId() == null) {
            return ResponseEntity.badRequest().body("Missing parameters");
        }

        if (!NicknameValidator.isValidNickname(nickname)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid nickname format");
        }

        if (!EmailValidator.isValidEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format");
        }

        final Server server = this.serverService.getServer(orderRequest.serverId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Server was not found"));

        final Product product = server.getProducts().stream()
                .filter(p -> p.getId().equals(orderRequest.productId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product was not found"));

        final Variant variant = product.getVariants().stream()
                .filter(v -> v.getId().equals(orderRequest.variantId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Variant was not found"));

        final UUID orderId = UUID.randomUUID();

        final Order orderModel = new Order(orderId);

        orderModel.setServerId(server.getId());
        orderModel.setProductId(product.getId());
        orderModel.setVariantId(variant.getId());

        //final int quantity = orderRequest.quantity() == 0 ? 1 : orderRequest.quantity();
        final String paymentChannel = orderRequest.paymentChannel() == null ? "" : orderRequest.paymentChannel();

        orderModel.setNickname(nickname);
        orderModel.setEmail(email);
        //orderModel.setQuantity(quantity);
        orderModel.setPaymentChannel(paymentChannel);

        final double finalPrice = variant.getPrice(); //* quantity;

        orderModel.setTotalPrice(finalPrice); // Add payment channel provision

        final String paymentTitle = String.format("Order for product '%s', variant '%s'", product.getName(), variant.getName());

        final CashBillPayment payment = new CashBillPayment(
                paymentTitle,
                new CashBillAmountData(finalPrice, "PLN"));

        payment.setAdditionalData(orderId.toString());
        payment.setDescription(PAYMENT_DESCRIPTION);

        payment.setPersonalData(new CashBillPersonalData()
                .setEmail(email));

        payment.setPaymentChannel(paymentChannel);

        payment.setReturnUrl(this.shopUrl + "/order/" + orderId);
        payment.setNegativeReturnUrl(this.shopUrl + "/something-went-wrong");

        try {
            final CashBillGeneratedPayment generatedPayment = this.paymentProcessor.createPayment(payment);

            orderModel.setCashBillId(generatedPayment.getId());
            orderModel.setCashBillLink(generatedPayment.getRedirectUrl());

            orderModel.setStatus(OrderStatus.CREATED);

            final Order order = this.orderService.saveOrder(orderModel);

            return ResponseEntity.ok(order);
        } catch (CashBillPaymentException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public List<Order> getOrders() {
        return this.orderService.getOrders();
    }
}
