package com.nguyencongvan.user_management.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nguyencongvan.user_management.dto.response.ApiResponse;
import com.nguyencongvan.user_management.exception.ErrorCode;
import com.nguyencongvan.user_management.service.PermissionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Log4j2
public class DynamicPermissionFilter extends OncePerRequestFilter {
    private static final List<IgnoredRequest> IGNORED_REQUESTS = List.of(
            new IgnoredRequest("/user-management/users", "POST"),
            new IgnoredRequest("/user-management/auth/token", "POST"),
            new IgnoredRequest("/user-management/auth/introspect", "POST"),
            new IgnoredRequest("/user-management/auth/logout", "POST"),
            new IgnoredRequest("/user-management/auth/refresh", "POST"),
            new IgnoredRequest("/user-management/v3/api-docs/**", "GET"),
            new IgnoredRequest("/user-management/swagger-ui/**", "GET"),
            new IgnoredRequest("/user-management/swagger-ui.html", "GET")
    );


    @Autowired
    private PermissionService permissionService;

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException, ServletException {

        String requestURI = request.getRequestURI();
        String httpMethod = request.getMethod();

        log.info("Request URI: " + requestURI + " Method: " + httpMethod);

        // Bỏ qua các request được cấu hình public (method + path)
        for (IgnoredRequest ignored : IGNORED_REQUESTS) {
            if (ignored.method().equalsIgnoreCase(httpMethod)
                    && pathMatcher.match(ignored.path(), requestURI)) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            writeErrorResponse(response, ErrorCode.UNAUTHENTICATED);
            return;
        }

        boolean hasPermission = permissionService.checkPermission(auth, requestURI, httpMethod);

        if (!hasPermission) {
            writeErrorResponse(response, ErrorCode.UNAUTHORIZED);
        }

        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatusCode().value());
        response.setContentType("application/json");

        ApiResponse<?> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.getWriter().flush();
    }

    private record IgnoredRequest(String path, String method) {}

}