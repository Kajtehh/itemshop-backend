package pl.kajteh.itemshop.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.kajteh.itemshop.security.ApiKeyInterceptor;

@Configuration
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemShopWebConfig implements WebMvcConfigurer {

    private final ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.apiKeyInterceptor);
    }
}
