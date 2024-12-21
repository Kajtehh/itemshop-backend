package pl.kajteh.itemshop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kajteh.payment.CashBillShop;

@Configuration
@ConfigurationProperties(prefix = "shop")
public class ItemShopConfig {

    private String id;
    private String secretKey;

    @Bean
    public CashBillShop shop() {
        return new CashBillShop(this.id, this.secretKey);
    }
}