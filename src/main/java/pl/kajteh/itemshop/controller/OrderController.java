package pl.kajteh.itemshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/{version}/orders")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class OrderController {
}
