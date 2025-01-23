package pl.kajteh.itemshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kajteh.payment.CashBillPaymentProcessor;

@Configuration
public class ItemShopConfig {

    @Value("${shop.id}")
    private String shopId;

    @Value("${shop.secretKey}")
    private String shopSecretKey;

    @Value("${shop.test}")
    private boolean shopTest;

    @Bean
    public CashBillPaymentProcessor paymentProcessor() {
        return new CashBillPaymentProcessor(this.shopId, this.shopSecretKey, this.shopTest);
    }
}