package pl.kajteh.itemshop.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<ApiVersionFilter> apiVersionFilterRegistration(ApiVersionFilter filter) {
        final FilterRegistrationBean<ApiVersionFilter> registration = new FilterRegistrationBean<>(filter);

        registration.addUrlPatterns("/api/*");
        return registration;
    }
}