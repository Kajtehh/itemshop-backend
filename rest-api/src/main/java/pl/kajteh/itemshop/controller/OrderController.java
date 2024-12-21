package pl.kajteh.itemshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
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
import pl.kajteh.payment.CashBillShop;
import pl.kajteh.payment.data.CashBillAmountData;
import pl.kajteh.payment.data.CashBillGeneratedPayment;
import pl.kajteh.payment.data.CashBillPersonalData;

@RestController
@RequestMapping("/api/{version}/orders")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderController {

    private final CashBillShop shop;
    private final OrderService orderService;
    private final ServerService serverService;

    private static final String PAYMENT_DESCRIPTION = "Created with ❤️ by Kajteh";

    @PostMapping("/start")
    public ResponseEntity<?> startOrder(@RequestBody OrderRequest orderRequest) {
        final Server server = this.serverService.get(orderRequest.serverId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Server was not found"));

        final Product product = server.getProducts().stream()
                .filter(p -> p.getId().equals(orderRequest.productId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product was not found"));

        final Variant variant = product.getVariants().stream()
                .filter(v -> v.getId().equals(orderRequest.variantId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Variant was not found"));

        final String nickname = orderRequest.nickname();
        final String email = orderRequest.email();

        if (!NicknameValidator.isValidNickname(nickname)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid nickname format.");
        }

        if (!EmailValidator.isValidEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email format.");
        }

        final Order orderModel = new Order();

        orderModel.setNickname(nickname);
        orderModel.setEmail(email);
        orderModel.setQuantity(orderModel.getQuantity() != 0 ? orderModel.getQuantity() : 1);
        orderModel.setStatus(OrderStatus.CREATED);

        final Order order = this.orderService.save(orderModel);

        final String paymentTitle = String.format("Product %s order, variant %s", product.getName(), variant.getName());

        final CashBillPayment payment = new CashBillPayment(
                paymentTitle,
                new CashBillAmountData(variant.getPrice(), "PLN"));

        payment.setAdditionalData(order.getId().toString());
        payment.setDescription(PAYMENT_DESCRIPTION);

        payment.setPersonalData(CashBillPersonalData.builder()
                .email(email)
                .build());

        payment.setPaymentChannel(orderRequest.paymentChannel());

        payment.setReturnUrl("https://kajteh.pl/order/" + order.getId());
        payment.setNegativeReturnUrl("https://kajteh.pl/something-went-wrong");

        try {
            final CashBillGeneratedPayment generatedPayment = this.shop.createPayment(payment);

            return ResponseEntity.ok(generatedPayment);
        } catch (CashBillPaymentException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
