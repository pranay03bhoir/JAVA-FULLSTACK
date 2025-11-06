package com.ecommerce.sbecom.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthEntryPoint - Unauthorized Access Handler
 * <p>
 * Purpose: Jab koi user bina authentication ke secured resource access karne ki koshish kare,
 * toh ye class automatically trigger hoti hai aur proper error response return karti hai
 * <p>
 * Real-world example:
 * - User bina login kiye /api/orders access karne ki koshish kare
 * - Invalid JWT token bheje
 * - Expired token bheje
 * - Token header mein hi na ho
 * <p>
 * Ye sab cases mein ye class execute hoti hai aur 401 Unauthorized response return karti hai
 */
@Component // Spring Bean banata hai - Spring Security automatically inject karega
public class AuthEntryPoint implements AuthenticationEntryPoint {

    /**
     * Logger instance - Error tracking aur debugging ke liye
     * SLF4J (Simple Logging Facade for Java) use kar rahe hain
     * <p>
     * Production mein ye logs file mein ya centralized logging system mein jate hain
     * Example: ELK Stack (Elasticsearch, Logstash, Kibana)
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPoint.class);

    /**
     * commence() Method - Core Method
     * <p>
     * Ye method automatically call hota hai jab:
     * 1. User authenticated nahi hai aur secured resource access karne ki koshish kare
     * 2. JWT token invalid/expired ho
     * 3. AuthenticationException throw ho
     *
     * @param request       - Incoming HTTP request (URL, headers, etc.)
     * @param response      - HTTP response jo client ko bhejni hai
     * @param authException - Authentication failure ki details (reason)
     *                      <p>
     *                      Flow:
     *                      Request → JWT Filter (validation fails) → AuthenticationException throw
     *                      → Spring Security → AuthEntryPoint.commence() call → JSON error response
     */
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {

        // ====== STEP 1: ERROR LOGGING ======
        /**
         * Error ko server logs mein record karo
         *
         * Benefits:
         * - Debugging ke liye helpful
         * - Security monitoring (kitne unauthorized attempts ho rahe hain)
         * - Audit trail maintain karne ke liye
         *
         * Log format: "Unauthorized error: Full authentication is required to access this resource"
         * Ya: "Unauthorized error: JWT token has expired"
         */
        logger.error("Unauthorized error: {}", authException.getMessage());

        // ====== STEP 2: RESPONSE CONTENT TYPE SET KARO ======
        /**
         * Response ka content type JSON set karo
         *
         * MediaType.APPLICATION_JSON_VALUE = "application/json"
         *
         * Isse client ko pata chalta hai ki response JSON format mein hai
         * Frontend easily parse kar sakta hai
         */
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // ====== STEP 3: HTTP STATUS CODE SET KARO ======
        /**
         * 401 Unauthorized status code set karo
         *
         * HttpServletResponse.SC_UNAUTHORIZED = 401
         *
         * HTTP Status Codes meaning:
         * - 200: Success
         * - 401: Unauthorized (authentication required)
         * - 403: Forbidden (authenticated but no permission)
         * - 404: Not Found
         * - 500: Server Error
         *
         * 401 ka matlab: "Tum kaun ho? Login karo pehle!"
         */
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // ====== STEP 4: ERROR RESPONSE BODY BANAO ======
        /**
         * JSON response body ke liye Map create karo
         * Ye Map JSON object ban jayega
         *
         * Structure:
         * {
         *   "Status": 401,
         *   "Error": "Unauthorized",
         *   "Message": "Full authentication is required...",
         *   "Path": "/api/orders"
         * }
         */
        final Map<String, Object> body = new HashMap<>();

        /**
         * Status field - HTTP status code (401)
         * Frontend isse programmatically check kar sakta hai
         */
        body.put("Status", HttpServletResponse.SC_UNAUTHORIZED);

        /**
         * Error field - Error type ("Unauthorized")
         * User-friendly error category
         */
        body.put("Error", "Unauthorized");

        /**
         * Message field - Detailed error message
         *
         * Possible messages:
         * - "Full authentication is required to access this resource"
         * - "JWT token has expired"
         * - "Invalid JWT signature"
         * - "JWT token is unsupported"
         *
         * Ye message AuthenticationException se aata hai
         */
        body.put("Message", authException.getMessage());

        /**
         * Path field - Kaunsa URL user access karne ki koshish kar raha tha
         *
         * Example:
         * - /api/orders
         * - /api/users/profile
         * - /api/admin/dashboard
         *
         * Debugging aur logging ke liye useful
         */
        body.put("Path", request.getServletPath());

        // ====== STEP 5: JSON RESPONSE WRITE KARO ======
        /**
         * ObjectMapper - Jackson library ka class
         * Java objects ko JSON mein convert karta hai
         *
         * Purpose: HashMap ko JSON string mein convert karke response stream mein likhna
         *
         * Process:
         * HashMap → ObjectMapper → JSON String → HTTP Response
         *
         * Example output:
         * {
         *   "Status": 401,
         *   "Error": "Unauthorized",
         *   "Message": "JWT token has expired",
         *   "Path": "/api/orders"
         * }
         */
        final ObjectMapper mapper = new ObjectMapper();

        /**
         * writeValue() method:
         * - First parameter: response.getOutputStream() - Yahan JSON likhna hai
         * - Second parameter: body (HashMap) - Ye JSON mein convert karna hai
         *
         * Ye method automatically:
         * 1. HashMap ko JSON format mein convert karta hai
         * 2. Response output stream mein write karta hai
         * 3. Client ko send kar deta hai
         */
        mapper.writeValue(response.getOutputStream(), body);
    }
}
