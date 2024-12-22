package pl.kajteh.itemshop.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiVersionFilter implements Filter {

    @Value("${api.version}")
    private String apiVersion;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;

        final String path = httpRequest.getRequestURI();
        final String[] pathSegments = path.split("/");

        final String version = pathSegments.length > 2 ? pathSegments[2] : null;

        if(version == null || !version.equals(this.apiVersion)) {
            httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpResponse.getWriter().write("Unsupported API version: " + version);
            return;
        }

        chain.doFilter(httpRequest, httpResponse);
    }
}
