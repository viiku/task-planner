package com.vikku.authservice.filter;

import com.vikku.authservice.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * {@code JwtAuthTokenFilter} is a custom Spring Security filter that executes
 * once per HTTP request to validate and authenticate users using JWT tokens.
 * <p>
 * It intercepts all incoming requests (except public endpoints like
 * <code>/api/auth/signin</code>, <code>/signup</code>, etc.) and performs the following steps:
 * <ol>
 *   <li>Extracts the JWT token from the request header (typically <b>Authorization: Bearer &lt;token&gt;</b>).</li>
 *   <li>Validates the token using {@link JwtService}.</li>
 *   <li>If valid, loads the user details and sets the authentication in Spring’s {@link SecurityContextHolder}.</li>
 *   <li>If invalid or missing, the filter allows the chain to continue — Spring Security will later delegate
 *       to {@link AuthEntryPointJwt} which returns a 401 Unauthorized response.</li>
 * </ol>
 *
 * <h3>Behavior Summary</h3>
 * <ul>
 *   <li>✅ Skips authentication for login, signup, and refresh token endpoints.</li>
 *   <li>✅ Automatically sets the authenticated user in the security context if JWT is valid.</li>
 *   <li>⚠️ Logs and continues the filter chain for invalid tokens instead of throwing runtime exceptions.</li>
 * </ul>
 *
 * <h3>Use Cases</h3>
 * <ul>
 *   <li>Protects REST endpoints that require authentication via JWT.</li>
 *   <li>Allows stateless authentication (no HTTP session required).</li>
 *   <li>Ensures that any request with a valid token gets an authenticated principal in Spring Security context.</li>
 * </ul>
 *
 * <h3>Typical Configuration</h3>
 * <p>
 * This filter is registered in the Spring Security filter chain via:
 * <pre>{@code
 * http.addFilterBefore(jwtAuthTokenFilter, UsernamePasswordAuthenticationFilter.class);
 * }</pre>
 *
 * @see AuthEntryPointJwt
 * @see JwtService
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthTokenFilter.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();
        // Skip authentication for public endpoints
        if (path.startsWith("/api/auth/signin")
                || path.startsWith("/api/auth/signup")
                || path.startsWith("/api/auth/refreshtoken")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = jwtService.parseJwt(request);
            if (jwt != null && jwtService.validateJwtToken(jwt)) {
                String username = jwtService.getUsernameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
