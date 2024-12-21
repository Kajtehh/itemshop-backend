package pl.kajteh.itemshop.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.util.Optional;

public class ApiKeyFilter implements Filter {

    @Value("${api.key}")
    private String apiKey;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        final Optional<String> authorizationHeader = Optional.ofNullable(httpRequest.getHeader("Authorization"));

        final boolean isAuthorized = authorizationHeader
                .filter(header -> header.startsWith("Bearer "))
                .map(header -> header.substring(7))
                .filter(this.apiKey::equals)
                .isPresent();

        if(this.isApiKeyRequired(httpRequest) && !isAuthorized) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Invalid or missing API key.");
            return;
        }

        chain.doFilter(request, response);
    }

    public boolean isApiKeyRequired(HttpServletRequest request) {
        final HandlerMethod handlerMethod = (HandlerMethod) request.getAttribute(HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE);

        return handlerMethod != null &&
                (handlerMethod.getMethodAnnotation(RequireApiKey.class) != null ||
                        handlerMethod.getBeanType().isAnnotationPresent(RequireApiKey.class));
    }
}
