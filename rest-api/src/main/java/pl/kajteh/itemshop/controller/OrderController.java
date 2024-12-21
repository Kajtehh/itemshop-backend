package pl.kajteh.itemshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kajteh.payment.CashBillShop;

@RequestMapping("/api/{version}/orders")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderController {

    private final CashBillShop shop;
}
