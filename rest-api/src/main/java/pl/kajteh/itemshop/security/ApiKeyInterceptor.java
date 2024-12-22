package pl.kajteh.itemshop.security;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String API_KEY_HEADER = "Authorization";

    @Value("${api.key}")
    private String requiredApiKey;

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler)
            throws IOException {
        if(!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        if(!handlerMethod.getMethod().isAnnotationPresent(RequireApiKey.class)
                && !handlerMethod.getBeanType().isAnnotationPresent(RequireApiKey.class)) {
            return true;
        }

        final String authHeader = request.getHeader(API_KEY_HEADER);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return false;
        }

        final String apiKey = authHeader.substring(7).trim();

        if(!apiKey.equals(this.requiredApiKey)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Invalid API Key");
            return false;
        }

        return true;
    }
}
