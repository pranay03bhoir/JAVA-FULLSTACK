package com.ecommerce.sbecom.security.jwt;

import com.ecommerce.sbecom.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

/**
 * Utility class for JWT (JSON Web Token) operations including token generation, validation,
 * and extraction of user information from tokens.
 */
@Component
public class JwtUtils {
    // Logger for logging JWT-related events and errors
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    // JWT token expiration time in milliseconds (configured in application.properties)
    @Value("${spring.app.jwtExpirationMs}")
    private Long jwtExpirationMs;
    // Secret key used for signing and verifying JWT tokens (configured in application.properties)
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtCookieName}")
    private String jwtCookie;

    /**
     * Extracts the JWT token from the Authorization header of an HTTP request.
     * The expected format is: "Bearer <token>"
     *
     * @param request The HTTP request containing the Authorization header
     * @return The extracted JWT token or null if not found/invalid format
     */
    public String getJwtFromHeader(HttpServletRequest request) {
        // Get the Authorization header from the request
        String bearerToken = request.getHeader("Authorization");
        logger.debug("Authorization Header: {}", bearerToken);

        // Check if the Authorization header exists and follows the 'Bearer ' scheme
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // Return the token part (remove 'Bearer ' prefix)
            return bearerToken.substring(7);
        }
        return null;
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return null;
        }
    }


    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt)
                .path("/api")
                .maxAge(24 * 60 * 60)
                .httpOnly(false)
                .build();
        return cookie;
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null)
                .path("/api")
                .build();
        return cookie;
    }

    /**
     * Generates a new JWT token for the specified user.
     *
     * @param userDetails User details containing the username
     * @return A signed JWT token containing the username and expiration date
     */
    public String generateTokenFromUsername(String username) {
//        String username = userDetails.getUsername();

        // Build the JWT with:
        // - Subject (username)
        // - Issued at (current time)
        // - Expiration time (current time + configured expiration)
        // - Signed with the secret key
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(jwtExpirationMs)))
                .signWith(key())
                .compact();
    }

    /**
     * Extracts the username from a JWT token.
     *
     * @param token The JWT token to extract the username from
     * @return The username (subject) from the token
     */
    public String getUsernameFromJwtToken(String token) {
        // Parse the token, verify its signature, and extract the subject (username)
        return Jwts.parser()
                .verifyWith((SecretKey) key())  // Verify using our secret key
                .build()
                .parseSignedClaims(token)       // Parse and verify the token
                .getPayload()                   // Get the token payload
                .getSubject();                  // Extract the subject (username)
    }

    /**
     * Generates a cryptographic key from the base64-encoded secret.
     * This key is used for both signing and verifying JWT tokens.
     *
     * @return A Key object for JWT signing/verification
     */
    public Key key() {
        // Convert the base64-encoded secret into a Key object using HMAC-SHA algorithm
        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(jwtSecret)
        );
    }

    /**
     * Validates a JWT token.
     *
     * @param authToken The JWT token to validate
     * @return true if the token is valid, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            // Attempt to parse and verify the token
            Jwts.parser()
                    .verifyWith((SecretKey) key())  // Verify using our secret key
                    .build()
                    .parseSignedClaims(authToken);  // This will throw an exception if invalid

            // If we get here, the token is valid
            return true;

        } catch (MalformedJwtException exception) {
            logger.error("Invalid JWT Token: {}", exception.getMessage());
        } catch (ExpiredJwtException exception) {
            logger.error("JWT Token is Expired: {}", exception.getMessage());
        } catch (UnsupportedJwtException exception) {
            logger.error("JWT Token is Unsupported: {}", exception.getMessage());
        } catch (IllegalArgumentException exception) {
            logger.error("JWT Claims string is empty: {}", exception.getMessage());
        }

        // If any exception was caught, the token is invalid
        return false;
    }
}
