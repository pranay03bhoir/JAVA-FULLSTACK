package com.ecommerce.sbecom.security.jwt;

// Import necessary Java and Spring Framework classes

import com.ecommerce.sbecom.security.services.UserDetailsServiceImpl;
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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter - Har Request Pe JWT Token Validate Karta Hai
 * <p>
 * Purpose: Ye filter har incoming HTTP request ko intercept karta hai aur:
 * 1. JWT token extract karta hai request header se
 * 2. Token ko validate karta hai
 * 3. Valid token se user details extract karta hai
 * 4. User ko SecurityContext mein authenticate kar deta hai
 * <p>
 * OncePerRequestFilter ko extend kar rahe hain taaki ye filter ek hi baar execute ho per request
 * (Kabhi kabhi filters multiple times execute ho sakte hain forwarding/including ke time)
 * <p>
 * Real-world flow:
 * User login → JWT token milta hai → Har subsequent request mein token bhejta hai
 * → Ye filter token validate karta hai → User authenticated ho jata hai
 * <p>
 * Example:
 * GET /api/orders
 * Headers: Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
 * → Ye filter token validate karega → User context set karega → Controller execute hoga
 */
@RequiredArgsConstructor
@Component// Spring Bean - Automatically SecurityConfig mein inject hoga
public class AuthTokenFilter extends OncePerRequestFilter {

    /**
     * Logger Instance - Debugging aur error tracking ke liye
     * <p>
     * SLF4J (Simple Logging Facade for Java) use kar rahe hain
     * Production mein ye logs file mein save hote hain
     * <p>
     * Log levels:
     * - DEBUG: Detailed information for debugging (development mein useful)
     * - INFO: General information
     * - ERROR: Error conditions
     * <p>
     * Example log: "AuthTokenFilter called for URI: /api/orders"
     */
    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    /**
     * JwtUtils - JWT Token Operations Ke Liye Utility Class
     * <p>
     * Responsibilities:
     * - Token ko validate karna (signature, expiration check)
     * - Token se username extract karna
     * - Token se claims (user info, roles) extract karna
     * - Request header se token parse karna
     *
     * @Autowired: Spring automatically inject karega is dependency ko
     * Field injection use kar rahe hain (generally constructor injection better hai)
     */

    private final JwtUtils jwtUtils;

    /**
     * UserDetailsServiceImpl - Database Se User Details Load Karta Hai
     * <p>
     * Ye aapki UserDetailsServiceImpl implementation hai jo:
     * 1. Username/email se user ko database se fetch karta hai
     * 2. UserDetailsImpl object return karta hai with user info and roles
     * <p>
     * Purpose: JWT token mein sirf username hota hai, baaki details
     * (like roles, permissions) database se load karna padta hai
     *
     * @Autowired: Spring automatically inject karega
     */

    private final UserDetailsServiceImpl userDetailsService;

    /**
     * doFilterInternal() - Main Filter Method (Core Logic)
     * <p>
     * Ye method HAR HTTP request ke liye automatically execute hota hai
     * (except static resources jo WebSecurityCustomizer mein ignore kiye hain)
     * <p>
     * Execution Order:
     * Request → AuthTokenFilter → Other Filters → Controller
     *
     * @param request     - Incoming HTTP request object
     *                    Contains: URL, headers, parameters, body
     *                    Access: request.getHeader("Authorization")
     * @param response    - HTTP response object
     *                    Ye filter response modify nahi karta, sirf pass through
     * @param filterChain - Chain of filters
     *                    filterChain.doFilter() call karne se next filter execute hota hai
     *                    Agar ye call nahi karoge, request stuck ho jayegi
     *                    <p>
     *                    Flow:
     *                    1. Token extract karo
     *                    2. Validate karo
     *                    3. User details load karo
     *                    4. SecurityContext mein set karo
     *                    5. Next filter ko pass karo
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // ====== STEP 0: REQUEST LOGGING ======
        /**
         * Debug log - Kaun sa URL access ho raha hai
         *
         * Example logs:
         * - "AuthTokenFilter called for URI: /api/auth/login"
         * - "AuthTokenFilter called for URI: /api/orders"
         * - "AuthTokenFilter called for URI: /api/users/profile"
         *
         * Development mein helpful hai flow samajhne ke liye
         * Production mein DEBUG logs disabled rehte hain (performance ke liye)
         */
        logger.debug("AuthTokenFilter called for URI: {}", request.getRequestURI());

        try {
            // ====== STEP 1: JWT TOKEN EXTRACT KARO ======
            /**
             * Request header se JWT token nikalo
             *
             * Header format: "Authorization: Bearer eyJhbGciOiJIUzI1NiIs..."
             *
             * parseJwt() method:
             * - "Authorization" header se token extract karta hai
             * - "Bearer " prefix remove karta hai
             * - Clean token return karta hai
             *
             * Possible outcomes:
             * - Valid token string (if header present and properly formatted)
             * - null (if no Authorization header ya invalid format)
             */
            String jwt = parseJwt(request);

            // ====== STEP 2: TOKEN VALIDATE KARO ======
            /**
             * Token validation aur username extraction
             *
             * Two conditions check kar rahe hain:
             *
             * Condition 1: jwt != null
             * - Token present hai ya nahi
             * - Public endpoints ke liye token nahi hota (login, register)
             *
             * Condition 2: jwtUtils.validateJwtToken(jwt)
             * - Token valid hai ya nahi
             * - Checks:
             *   a) Signature valid hai? (token tampered toh nahi?)
             *   b) Token expired toh nahi?
             *   c) Token format correct hai?
             *   d) Token claims valid hain?
             *
             * Agar dono conditions true hain, tab hi user authenticate karenge
             * Otherwise, authentication skip ho jayega (unauthenticated request)
             */
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // ====== STEP 3: USERNAME EXTRACT KARO JWT SE ======
                /**
                 * JWT token se username/email extract karo
                 *
                 * JWT structure:
                 * {
                 *   "sub": "user@example.com",  ← Subject (username)
                 *   "iat": 1635123456,          ← Issued at time
                 *   "exp": 1635209856           ← Expiration time
                 * }
                 *
                 * jwtUtils.getUsernameFromJwtToken(jwt) method "sub" claim return karta hai
                 *
                 * Example: username = "rahul@example.com"
                 */
                String username = jwtUtils.getUsernameFromJwtToken(jwt);
                // ====== STEP 4: DATABASE SE USER DETAILS LOAD KARO ======
                /**
                 * Username se complete user details load karo database se
                 *
                 * Process:
                 * 1. userDetailsService.loadUserByUsername("rahul@example.com") call
                 * 2. UserDetailsServiceImpl execute hota hai
                 * 3. UserRepository se user fetch hota hai
                 * 4. UserDetailsImpl object return hota hai
                 *
                 * UserDetails contains:
                 * - Username/Email
                 * - Password (encrypted)
                 * - Roles/Authorities (e.g., ROLE_CUSTOMER, ROLE_ADMIN)
                 * - Account status (enabled, locked, expired)
                 *
                 * Why load from database?
                 * - JWT mein sirf username hota hai (token size chhota rakhne ke liye)
                 * - Roles/permissions database se load karne padte hain
                 * - Latest user status check hota hai (disabled/locked users)
                 */
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // ====== STEP 5: AUTHENTICATION TOKEN CREATE KARO ======
                /**
                 * Spring Security ka Authentication object banao
                 *
                 * UsernamePasswordAuthenticationToken constructor:
                 *
                 * Parameter 1: principal (userDetails)
                 * - Authenticated user ki identity
                 * - Ye object SecurityContext mein store hoga
                 * - Controller mein access kar sakte ho:
                 *   @AuthenticationPrincipal UserDetailsImpl user
                 *
                 * Parameter 2: credentials (null)
                 * - Password field
                 * - Null kar rahe hain kyunki JWT already validated hai
                 * - Password store karne ki zaroorat nahi (security risk)
                 *
                 * Parameter 3: authorities (userDetails.getAuthorities())
                 * - User ke roles aur permissions
                 * - Example: [ROLE_CUSTOMER, ROLE_USER]
                 * - Ye authorization ke liye use hote hain
                 * - @PreAuthorize("hasRole('ADMIN')") checks ye authorities
                 *
                 * Authentication token two states mein hota hai:
                 * - Unauthenticated: Login time (password with token)
                 * - Authenticated: After validation (no password needed)
                 *
                 * Ye authenticated token hai (3 parameters constructor)
                 */
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,                    // Who is the user?
                                null,                           // Password not needed
                                userDetails.getAuthorities()    // What can user do?
                        );

                // ====== STEP 6: REQUEST DETAILS ADD KARO ======
                /**
                 * Authentication object mein HTTP request ki details add karo
                 *
                 * WebAuthenticationDetailsSource:
                 * - Request se additional details extract karta hai
                 * - IP address, session ID, etc.
                 *
                 * buildDetails(request):
                 * - Creates WebAuthenticationDetails object
                 * - Contains: Remote IP address, Session ID
                 *
                 * Why add details?
                 * - Audit logging (kaun se IP se request aayi)
                 * - Security tracking (suspicious activity detection)
                 * - Session management
                 *
                 * Example details:
                 * - remoteAddress: "192.168.1.100"
                 * - sessionId: "ABC123XYZ"
                 */
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // ====== STEP 7: SECURITY CONTEXT MEIN SET KARO ======
                /**
                 * User ko Spring Security context mein authenticate kar do
                 *
                 * SecurityContextHolder:
                 * - Thread-local storage for authentication
                 * - Har thread (request) ka apna security context hota hai
                 *
                 * getContext().setAuthentication(authentication):
                 * - Current thread ke SecurityContext mein authentication set karta hai
                 *
                 * Impact:
                 * 1. Spring Security ab is user ko authenticated manega
                 * 2. @PreAuthorize, @Secured annotations kaam karenge
                 * 3. SecurityContextHolder.getContext().getAuthentication()
                 *    se kahin bhi access kar sakte ho
                 * 4. Controller methods mein @AuthenticationPrincipal kaam karega
                 *
                 * Example controller usage:
                 * @GetMapping("/profile")
                 * public User getProfile(@AuthenticationPrincipal UserDetailsImpl user) {
                 *     // Direct access to authenticated user
                 * }
                 *
                 * After this line, user is FULLY AUTHENTICATED!
                 */
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // ====== STEP 8: ROLES LOGGING (DEBUGGING) ======
                /**
                 * User ke roles ko log karo debugging ke liye
                 *
                 * userDetails.getAuthorities() returns:
                 * [SimpleGrantedAuthority("ROLE_CUSTOMER"), SimpleGrantedAuthority("ROLE_USER")]
                 *
                 * Log example:
                 * "Roles from JWT: [ROLE_CUSTOMER, ROLE_ADMIN]"
                 *
                 * Development mein helpful hai:
                 * - Verify karne ke liye ki correct roles load ho rahe hain
                 * - Authorization issues debug karne ke liye
                 *
                 * Production mein:
                 * - DEBUG logs disabled hote hain (performance)
                 * - Sensitive information leak nahi hoti
                 */
                logger.debug("Roles from JWT: {}", userDetails.getAuthorities());
            }
            // If jwt is null or invalid, skip authentication
            // Request will continue as unauthenticated
            // If accessing secured endpoint, AuthEntryPoint will trigger

        } catch (Exception e) {
            // ====== ERROR HANDLING ======
            /**
             * Koi bhi exception catch karo aur log karo
             *
             * Possible exceptions:
             * 1. JwtException - Token parsing/validation failed
             * 2. UsernameNotFoundException - User database mein nahi mila
             * 3. DataAccessException - Database connection issue
             * 4. NullPointerException - Unexpected null values
             *
             * Error log example:
             * "Cannot set user authentication: JWT signature does not match"
             * "Cannot set user authentication: User not found with email: xyz@example.com"
             *
             * Important: Exception ko swallow kar rahe hain (throw nahi kar rahe)
             * Kyunki:
             * - Request ko continue karne dena hai
             * - filterChain.doFilter() execute hona chahiye
             * - AuthEntryPoint proper 401 response dega if needed
             *
             * Agar exception throw karte, toh:
             * - Request abruptly fail ho jati
             * - Client ko generic 500 error milta
             * - Proper JSON error response nahi milta
             */
            logger.error("Cannot set user authentication: {}", e);
        }

        // ====== STEP 9: FILTER CHAIN CONTINUE KARO ======
        /**
         * Next filter ko execute karne do
         *
         * filterChain.doFilter(request, response):
         * - Request ko next filter mein pass karta hai
         * - Ya final destination (Controller) tak pohochata hai
         *
         * Filter chain order:
         * AuthTokenFilter → ExceptionHandlingFilter → AuthorizationFilter → Controller
         *
         * CRITICAL: Ye line HAMESHA execute honi chahiye
         * - Try block mein ho ya catch block mein ho
         * - Otherwise request hang ho jayegi
         * - Client ko response nahi milega
         *
         * Authenticated request:
         * - SecurityContext mein user set hai
         * - Controller mein user access kar sakte ho
         *
         * Unauthenticated request:
         * - SecurityContext empty hai
         * - Agar secured endpoint hai, AuthEntryPoint trigger hoga
         * - Agar public endpoint hai, normally execute hoga
         */
        filterChain.doFilter(request, response);
    }

    /**
     * parseJwt() - Helper Method To Extract JWT From Request Header
     * <p>
     * Purpose: Authorization header se JWT token extract karna
     * <p>
     * Header format: "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
     * <p>
     * Steps:
     * 1. jwtUtils.getJwtFromHeader(request) call karta hai
     * 2. JwtUtils internally:
     * - request.getHeader("Authorization") se header nikalta hai
     * - "Bearer " prefix check karta hai
     * - Prefix remove karke clean token return karta hai
     * <p>
     * Returns:
     * - Clean JWT token string (if valid header present)
     * - null (if Authorization header missing ya invalid format)
     *
     * @param request - HTTP request containing Authorization header
     * @return JWT token string or null
     */
    private String parseJwt(HttpServletRequest request) {
        /**
         * JwtUtils helper method se token extract karo
         *
         * Example:
         * Input header: "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIn0.xyz"
         * Output: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ1c2VyIn0.xyz"
         *
         * Agar Authorization header nahi hai:
         * Output: null
         */

        String jwtFromCookie = jwtUtils.getJwtFromCookie(request);
        if (jwtFromCookie != null) {
            return jwtFromCookie;
        }
        
        String jwtFromHeader = jwtUtils.getJwtFromHeader(request);
        if (jwtFromHeader != null) {
            return jwtFromHeader;
        }
        /**
         * Debug logging - Token present hai ya nahi
         *
         * Security best practice:
         * - Actual token ko log mein KABHI nahi print karo
         * - Token log files mein store ho jata hai (security risk)
         * - Sirf token presence log karo: "[JWT_TOKEN_PRESENT]" or "null"
         *
         * Log output:
         * - "Extracted JWT token: [JWT_TOKEN_PRESENT]" (if token exists)
         * - "Extracted JWT token: null" (if no token)
         *
         * Development tip:
         * - Agar debugging ke liye actual token dekhna ho, temporarily log kar sakte ho
         * - But production mein NEVER log actual tokens
         */
        logger.debug("Extracted JWT token: {}", jwtFromCookie != null ? "[JWT_TOKEN_PRESENT]" : "null");

        return null;
    }
}
