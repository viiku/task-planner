package com.vikku.taskplanner.authservice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * {@code AuthEntryPointJwt} is a custom implementation of
 * {@link org.springframework.security.web.AuthenticationEntryPoint} used to handle
 * unauthorized access attempts in the application.
 * <p>
 * When a client tries to access a protected resource without valid authentication
 * credentials (for example, missing or invalid JWT token), Spring Security delegates
 * the exception handling to this entry point.
 * <p>
 * This class intercepts the authentication failure and sends a well-structured JSON
 * response with HTTP 401 (Unauthorized) status instead of the default HTML error page.
 * <p>
 * The JSON response typically contains:
 * <ul>
 *   <li><b>status</b> — HTTP status code (401)</li>
 *   <li><b>error</b> — short error label ("Unauthorized")</li>
 *   <li><b>message</b> — a user-friendly message explaining the reason</li>
 *   <li><b>path</b> — the requested servlet path that triggered the error</li>
 *   <li><b>timestamp</b> — server time when the error occurred</li>
 * </ul>
 *
 * <h3>Example Scenario</h3>
 * <ul>
 *   <li>User sends a request to a secured API endpoint without a JWT token.</li>
 *   <li>Spring Security detects missing authentication and throws {@link AuthenticationException}.</li>
 *   <li>{@code AuthEntryPointJwt} handles this exception and returns a JSON response:
 *       <pre>
 *       {
 *         "status": 401,
 *         "error": "Unauthorized",
 *         "message": "You need to login first in order to perform this action.",
 *         "path": "/api/tasks",
 *         "timestamp": "2025-10-09T13:45:21.123"
 *       }
 *       </pre>
 *   </li>
 * </ul>
 *
 * <h3>Use Cases</h3>
 * <ul>
 *   <li>Protecting REST APIs that require authentication (e.g., JWT, session).</li>
 *   <li>Providing consistent JSON error responses to frontend clients (React, Angular, etc.).</li>
 *   <li>Handling expired or invalid JWT tokens gracefully.</li>
 * </ul>
 *
 * <p>
 * This class is typically registered automatically by Spring Security when included as a bean
 * and configured in the {@code SecurityFilterChain} via:
 * <pre>{@code
 * http.exceptionHandling()
 *     .authenticationEntryPoint(authEntryPointJwt);
 * }</pre>
 *
 * @author Vivek
 * @since 1.0
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        logger.error("Unauthorized error: {}", authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final Map<String, Object> body = new HashMap<>();
        body.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        body.put("error", "Unauthorized");
        body.put("message", "You need to login first in order to perform this action.");
        body.put("path", request.getServletPath());
        body.put("timestamp", LocalDateTime.now());

        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.writeValue(response.getOutputStream(), body);
    }
}
