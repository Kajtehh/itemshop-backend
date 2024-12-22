package pl.kajteh.itemshop.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.kajteh.itemshop.security.ApiVersionFilter;

@Configuration
public class ItemShopFilterConfig {

    @Bean
    public FilterRegistrationBean<ApiVersionFilter> apiVersionFilterRegistration(ApiVersionFilter filter) {
        final FilterRegistrationBean<ApiVersionFilter> registration = new FilterRegistrationBean<>(filter);

        registration.addUrlPatterns("/api/*");

        return registration;
    }
}