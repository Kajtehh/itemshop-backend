package pl.kajteh.itemshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kajteh.payment.CashBillShop;

@Configuration
public class ItemShopConfig {

    @Value("${shop.id}")
    private String shopId;

    @Value("${shop.secretKey}")
    private String shopSecretKey;

    @Value("${shop.test}")
    private boolean shopTest;

    @Bean
    public CashBillShop shop() {
        final CashBillShop shop = new CashBillShop(this.shopId, this.shopSecretKey);

        shop.setTest(this.shopTest);
        return shop;
    }
}