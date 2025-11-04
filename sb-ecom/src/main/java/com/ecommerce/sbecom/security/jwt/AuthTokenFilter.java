package com.ecommerce.sbecom.security.jwt;

// Import necessary Java and Spring Framework classes

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


/**
 * JWT Authentication Filter that processes each request to validate JWT tokens.
 * This filter is applied once per request to check for a valid JWT in the request header.
 */
@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    // Logger for debugging and error tracking
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    @Autowired
    private JwtUtils jwtUtils;  // Utility class for JWT operations
    @Autowired
    private UserDetailsService userDetailsService;  // Service to load user details

    /**
     * Processes each HTTP request to check for a valid JWT token.
     * If valid, it sets the authentication in the security context.
     *
     * @param request     The HTTP request
     * @param response    The HTTP response
     * @param filterChain The filter chain to continue processing the request
     * @throws ServletException If a servlet exception occurs
     * @throws IOException      If an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Log the URI being accessed
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());

        try {
            // 1. Extract JWT from the Authorization header
            String jwt = parseJwt(request);

            // 2. Validate the JWT token
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // 3. Extract username from JWT
                String username = jwtUtils.getUsernameFromJwtToken(jwt);

                // 4. Load user details from the database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Create authentication token with user details and authorities
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,  // Credentials are null as we're not using password here
                                userDetails.getAuthorities()  // User roles/permissions
                        );

                // 6. Add request details to the authentication object
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 7. Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 8. Log the roles for debugging
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
            }
        } catch (Exception e) {
            // Log any authentication errors
            logger.error("Cannot set user authentication: {}", e);
        }

        // 9. Continue the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     *
     * @param request The HTTP request containing the JWT token
     * @return The JWT token if found, null otherwise
     */
    private String parseJwt(HttpServletRequest request) {
        // Extract JWT from the Authorization header (format: "Bearer <token>")
        String jwt = jwtUtils.getJwtFromHeader(request);

        // Log the extracted token for debugging (be cautious with logging tokens in production)
        logger.debug("Extracted JWT token: {}", jwt != null ? "[JWT_TOKEN_PRESENT]" : "null");

        return jwt;
    }
}
