package com.ecommerce.sbecom.controllers;

import com.ecommerce.sbecom.models.AppRole;
import com.ecommerce.sbecom.models.Role;
import com.ecommerce.sbecom.models.User;
import com.ecommerce.sbecom.repositories.RoleRepository;
import com.ecommerce.sbecom.repositories.UserRepository;
import com.ecommerce.sbecom.security.jwt.JwtUtils;
import com.ecommerce.sbecom.security.request.LoginRequest;
import com.ecommerce.sbecom.security.request.SignupRequest;
import com.ecommerce.sbecom.security.response.MessageResponse;
import com.ecommerce.sbecom.security.response.UserInfoResponse;
import com.ecommerce.sbecom.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authentication Controller - User Login Handler
 * <p>
 * Ye controller authentication (login) requests ko handle karta hai
 * Purpose: User credentials validate karke JWT token generate karna
 */
@RestController // REST API endpoint banata hai - JSON request/response
@RequiredArgsConstructor // Lombok: final fields ke liye constructor inject karta hai
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    /**
     * AuthenticationManager - Spring Security ka core component
     * <p>
     * Kaam: User credentials (email/password) ko validate karna
     * <p>
     * Process:
     * 1. Username/password receive karta hai
     * 2. DaoAuthenticationProvider ke through database se validate karta hai
     * 3. Success: Authentication object return karta hai
     * 4. Failure: AuthenticationException throw karta hai
     * <p>
     * Spring automatically inject karega (constructor injection via @RequiredArgsConstructor)
     */
    private final AuthenticationManager authenticationManager;

    /**
     * JwtUtils - JWT Token Operations Utility
     * <p>
     * Responsibilities:
     * 1. JWT token generate karna (user details se)
     * 2. Token validate karna (signature, expiration)
     * 3. Token se username extract karna
     * <p>
     * Ye custom utility class hai jo aapne banai hai
     */
    private final JwtUtils jwtUtils;

    /**
     * Login Endpoint - User Authentication Handler
     * <p>
     * URL: POST /signin
     * Request Body: { "email": "user@example.com", "password": "password123" }
     * <p>
     * Success Response: {
     * "id": 1,
     * "username": "john_doe",
     * "roles": ["ROLE_CUSTOMER"],
     * "jwtToken": "eyJhbGciOiJIUzI1NiIs..."
     * }
     * <p>
     * Error Response: {
     * "status": false,
     * "message": "Bad credentials"
     * }
     *
     * @param loginRequest - Request body containing email and password
     * @return ResponseEntity with JWT token or error message
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        // ====== STEP 1: AUTHENTICATION ATTEMPT (TRY-CATCH BLOCK) ======
        /**
         * Authentication ko try-catch mein wrap kiya hai kyunki:
         * - Agar credentials wrong hain, AuthenticationException throw hota hai
         * - Exception ko catch karke user-friendly error response bhejni hai
         * - Application crash nahi honi chahiye bad credentials pe
         */
        Authentication authentication; // Local variable declare karo

        try {
            // ====== STEP 2: CREDENTIALS VALIDATE KARO ======
            /**
             * authenticationManager.authenticate() - Core authentication method
             *
             * Input: UsernamePasswordAuthenticationToken
             * - Unauthenticated token with username and password
             *
             * Process:
             * 1. DaoAuthenticationProvider ko delegate karta hai
             * 2. UserDetailsService se user load hota hai
             * 3. PasswordEncoder se password match hota hai
             * 4. Success: Authenticated token return
             * 5. Failure: AuthenticationException throw
             */
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),     // Username/Email - String
                            loginRequest.getPassword()   // Password - String
                    )
            );
            /**
             * Ye method call karne pe kya hota hai:
             *
             * 1. AuthenticationManager request receive karta hai
             * 2. DaoAuthenticationProvider ko delegate karta hai
             * 3. Provider userDetailsService.loadUserByUsername(username) call karta hai
             * 4. Database se User entity fetch hoti hai
             * 5. UserDetailsImpl object create hota hai
             * 6. passwordEncoder.matches(plainPassword, encryptedPassword) check hota hai
             * 7. Match success:
             *    - Authenticated UsernamePasswordAuthenticationToken return
             *    - Principal: UserDetailsImpl object
             *    - Authorities: User roles (ROLE_CUSTOMER, etc.)
             * 8. Match failure:
             *    - BadCredentialsException throw
             */

        } catch (AuthenticationException e) {
            // ====== STEP 3: AUTHENTICATION FAILURE HANDLING ======
            /**
             * Catch block - Credentials invalid hone pe execute hota hai
             *
             * Common exceptions:
             * - BadCredentialsException: Wrong password
             * - UsernameNotFoundException: User doesn't exist
             * - DisabledException: Account disabled
             * - LockedException: Account locked
             *
             * Error response structure:
             * {
             *   "status": false,
             *   "message": "Bad credentials"
             * }
             *
             * HTTP Status: 404 NOT_FOUND
             * ⚠️ ISSUE: 404 galat hai - use 401 UNAUTHORIZED
             */
            Map<String, Object> map = new HashMap<>();
            map.put("status", false);
            map.put("message", "Bad credentials");

            /**
             * ⚠️ FIX: HttpStatus.NOT_FOUND → HttpStatus.UNAUTHORIZED
             *
             * 401: Authentication failed (credentials wrong)
             * 404: Resource not found (URL wrong)
             *
             * CORRECTED:
             */
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
            /**
             * Ye response client ko milega:
             * Status Code: 401
             * Body: { "status": false, "message": "Bad credentials" }
             *
             * Frontend isse handle karke login form mein error message show karega
             */
        }

        // ====== STEP 4: SECURITY CONTEXT MEIN AUTHENTICATION SET KARO ======
        /**
         * Authentication successful - User authenticated ho gaya
         *
         * SecurityContextHolder.getContext().setAuthentication(authentication):
         * - Current thread ke SecurityContext mein authentication store karta hai
         * - Ab user fully authenticated hai application mein
         *
         * Impact:
         * 1. Subsequent requests mein user identity available rahegi
         * 2. @PreAuthorize, @Secured annotations kaam karenge
         * 3. SecurityContext se kahin bhi user access kar sakte ho
         *
         * Note: Ye step optional hai login endpoint ke liye
         * Kyunki hum JWT token generate karke de rahe hain
         * Future requests mein JWT se hi authentication hoga
         * But good practice hai set karna
         */
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // ====== STEP 5: AUTHENTICATED USER DETAILS EXTRACT KARO ======
        /**
         * Authentication object se UserDetailsImpl extract karo
         *
         * authentication.getPrincipal():
         * - Returns: Object (generic type)
         * - Actual type: UserDetailsImpl (jo authentication time set hua tha)
         * - Type cast karna padega: (UserDetailsImpl)
         *
         * UserDetailsImpl contains:
         * - id: User database ID
         * - username: User ka username
         * - email: User ka email
         * - password: Encrypted password
         * - authorities: User roles/permissions
         *
         * Why extract?
         * - JWT token generate karne ke liye user details chahiye
         * - Response mein user info bhejni hai
         */
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // ====== STEP 6: JWT TOKEN GENERATE KARO ======
        /**
         * JWT token generate karo user details se
         *
         * jwtUtils.generateTokenFromUsername(userDetails):
         * - Input: UserDetailsImpl object
         * - Process:
         *   1. Username extract karta hai
         *   2. JWT claims create karta hai (subject, issued date, expiration)
         *   3. Secret key se sign karta hai
         *   4. Token string generate karta hai
         * - Output: JWT token string
         *
         * Token structure:
         * eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9  ← Header
         * .eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNjM1MTIzNDU2fQ  ← Payload
         * .xyz123abc  ← Signature
         *
         * Client future requests mein ye token header mein bhejega:
         * Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
         */
        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        // ====== STEP 7: USER ROLES EXTRACT KARO ======
        /**
         * User ke authorities (roles) ko String list mein convert karo
         *
         * userDetails.getAuthorities():
         * - Returns: Collection<? extends GrantedAuthority>
         * - Example: [SimpleGrantedAuthority("ROLE_CUSTOMER"), SimpleGrantedAuthority("ROLE_USER")]
         *
         * Stream operations:
         * 1. .stream() - Collection ko stream mein convert
         * 2. .map(item -> item.getAuthority()) - Har authority ka naam extract
         * 3. .toList() - List<String> mein convert
         *
         * Result: ["ROLE_CUSTOMER", "ROLE_USER"]
         *
         * Purpose:
         * - Response mein user roles bhejne ke liye
         * - Frontend role-based UI render kar sakta hai
         * - Example: Admin menu sirf ROLE_ADMIN users ko dikhana
         */
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(item -> item.getAuthority()) // Ya: .map(GrantedAuthority::getAuthority)
                .toList();

        // ====== STEP 8: SUCCESS RESPONSE CREATE KARO ======
        /**
         * UserInfoResponse DTO object banao
         *
         * Contains:
         * - id: User database ID (1, 2, 3...)
         * - username: User ka display name
         * - roles: User ke roles list ["ROLE_CUSTOMER"]
         * - jwtToken: Generated JWT token
         *
         * Example response:
         * {
         *   "id": 1,
         *   "username": "john_doe",
         *   "roles": ["ROLE_CUSTOMER"],
         *   "jwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
         * }
         *
         * Frontend isko receive karke:
         * 1. Token ko localStorage/sessionStorage mein save karega
         * 2. User info display karega (username, roles)
         * 3. Future API calls mein token header mein bhejega
         */
        UserInfoResponse response = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                jwtToken
        );

        // ====== STEP 9: 200 OK RESPONSE RETURN KARO ======
        /**
         * ResponseEntity.ok(response):
         * - HTTP Status: 200 OK
         * - Body: UserInfoResponse object (Spring automatically JSON mein convert karega)
         * - Content-Type: application/json
         *
         * Client ko ye response milega:
         * Status: 200
         * Body: {
         *   "id": 1,
         *   "username": "john_doe",
         *   "roles": ["ROLE_CUSTOMER"],
         *   "jwtToken": "eyJhbG..."
         * }
         *
         * Login successful! User ab authenticated hai aur token use kar sakta hai
         */
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(response);
    }

    /**
     * User Registration Endpoint - New User Account Create Karta Hai
     * <p>
     * URL: POST /signup
     * Request Body: {
     * "username": "john_doe",
     * "email": "john@example.com",
     * "password": "password123",
     * "roles": ["seller", "user"]  // Optional
     * }
     * <p>
     * Success Response: {
     * "message": "User registered successfully"
     * }
     * <p>
     * Error Response: {
     * "message": "Error: Username already taken"
     * }
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        // ====== STEP 1: USERNAME DUPLICATE CHECK ======
        /**
         * Database mein check karo ki username already exist karta hai ya nahi
         *
         * userRepository.existsByUserName():
         * - Database query: SELECT COUNT(*) FROM users WHERE username = ?
         * - Returns: true if username exists, false otherwise
         *
         * Why check?
         * - Username unique hona chahiye (database constraint)
         * - User-friendly error message dena hai duplicate case mein
         * - Database constraint violation exception avoid karna hai
         *
         * Example scenario:
         * - New user tries username "john_doe"
         * - Already exists in database
         * - Return 400 Bad Request with error message
         */
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            /**
             * ResponseEntity.badRequest():
             * - HTTP Status: 400 Bad Request
             * - Indicates: Client sent invalid/duplicate data
             *
             * MessageResponse:
             * - Custom DTO for error messages
             * - Contains: String message field
             * - Frontend isse parse karke user ko show karega
             *
             * Response:
             * Status: 400
             * Body: { "message": "Error: Username already taken" }
             */
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username already taken"));
        }

        // ====== STEP 2: EMAIL DUPLICATE CHECK ======
        /**
         * Email uniqueness verify karo
         *
         * Why email check?
         * - Email bhi unique identifier hai
         * - Password reset/verification emails bhejne ke liye unique hona zaroori
         * - One email = One account policy
         *
         * Query: SELECT COUNT(*) FROM users WHERE email = ?
         *
         * Example:
         * - User enters: "john@example.com"
         * - Already registered by someone else
         * - Reject registration with appropriate error
         */
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            /**
             * Note: Typo in message - "exits" should be "exists"
             * Production mein correct spelling use karo
             *
             * Security consideration:
             * - Kabhi kabhi email existence hide karte hain (privacy)
             * - Generic message: "Registration failed" (attacker ko info nahi milti)
             * - But user experience ke liye specific message better hai
             */
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: email already exits"));
        }

        // ====== STEP 3: NEW USER OBJECT CREATE KARO ======
        /**
         * User entity instance banao with basic details
         *
         * Constructor parameters:
         * 1. username - Display name / login username
         * 2. email - Contact email / alternate login
         * 3. password - ENCRYPTED password (not plain text)
         *
         * passwordEncoder.encode(signupRequest.getPassword()):
         * - Plain text password: "password123"
         * - BCrypt encrypted: "$2a$10$abcd1234..." (60 characters)
         *
         * Why encode?
         * - Security: Plain passwords NEVER store in database
         * - If database leaked, passwords safe rehte hain
         * - BCrypt: Industry standard, salted hashing
         *
         * Example:
         * Input: "password123"
         * Encoded: "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy"
         *
         * Every time encode() call, different hash (salt)
         * But matches() method correctly verify karega
         */
        User user = new User(
                passwordEncoder.encode(signupRequest.getPassword()),
                signupRequest.getEmail(),
                signupRequest.getUsername()
        );

        // ====== STEP 4: ROLES EXTRACT FROM REQUEST ======
        /**
         * SignupRequest se roles list nikalo
         *
         * signupRequest.getRoles():
         * - Returns: Set<String> (e.g., ["admin", "seller"])
         * - Can be: null (no roles specified by user)
         * - Can be: empty Set<>
         * - Can be: ["user", "seller", "admin"]
         *
         * Purpose:
         * - User registration time pe roles assign karna
         * - Admin panel se user create kar rahe toh multiple roles possible
         * - Public registration pe usually null/default role
         *
         * Example request:
         * {
         *   "username": "john",
         *   "email": "john@example.com",
         *   "password": "pass123",
         *   "roles": ["seller"]  ← Ye field optional hai
         * }
         */
        Set<String> strRoles = signupRequest.getRoles();

        /**
         * Role entities store karne ke liye Set create karo
         *
         * Why Set?
         * - Duplicate roles avoid (Set property)
         * - User ka many-to-many relationship with Role
         * - Database: user_roles junction table
         *
         * HashSet vs ArrayList:
         * - HashSet: No duplicates, unordered
         * - Perfect for roles (no duplicate ROLE_ADMIN)
         */
        Set<Role> roles = new HashSet<>();

        // ====== STEP 5: ROLE ASSIGNMENT LOGIC ======
        /**
         * Conditional role assignment based on request
         *
         * Two scenarios:
         * A. strRoles == null: Default role assign karo
         * B. strRoles != null: Requested roles assign karo
         */
        if (strRoles == null) {
            // ====== SCENARIO A: DEFAULT ROLE (NULL REQUEST) ======
            /**
             * Jab user ne roles specify nahi kiye (public registration)
             *
             * Default behavior:
             * - Har user ko minimum ROLE_USER milna chahiye
             * - Basic access rights
             * - Logged-in user features use kar sake
             *
             * roleRepository.findByRoleName(AppRole.ROLE_USER):
             * - Database query: SELECT * FROM roles WHERE role_name = 'ROLE_USER'
             * - Returns: Optional<Role>
             *
             * .orElseThrow():
             * - Agar role database mein nahi mila
             * - RuntimeException throw karo
             * - Indicates: Database setup issue (roles table empty)
             *
             * Production consideration:
             * - Roles pre-populate hone chahiye database mein
             * - Application startup pe seed data run karo
             * - INSERT INTO roles VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN'), (3, 'ROLE_SELLER')
             */
            Role userRoles = roleRepository.findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found"));

            /**
             * Role ko Set mein add karo
             * - User ab ROLE_USER permissions ke saath authenticate hoga
             * - Basic features access kar payega
             */
            roles.add(userRoles);

        } else {
            // ====== SCENARIO B: CUSTOM ROLES (NON-NULL REQUEST) ======
            /**
             * User ne specific roles request kiye hain
             *
             * .forEach() loop:
             * - Har role string ko process karo
             * - Database se corresponding Role entity fetch karo
             * - roles Set mein add karo
             *
             * Example:
             * Input: ["admin", "seller"]
             * Process: admin → ROLE_ADMIN entity
             *          seller → ROLE_SELLER entity
             * Output: roles Set with 2 Role entities
             */
            strRoles.forEach(role -> {
                /**
                 * Switch statement - Role string ko Role entity mein convert karo
                 *
                 * Case sensitivity:
                 * - Input: lowercase ("admin", "seller")
                 * - Database: ENUM AppRole.ROLE_ADMIN, AppRole.ROLE_SELLER
                 *
                 * Why switch?
                 * - Multiple role types handle karna
                 * - Readable code
                 * - Easy to add new roles future mein
                 */
                switch (role) {
                    case "admin":
                        /**
                         * ADMIN ROLE ASSIGNMENT
                         *
                         * Security consideration:
                         * ⚠️ PRODUCTION WARNING: Public signup pe admin role NAHI dena chahiye!
                         *
                         * Current code issue:
                         * - Koi bhi user signup time "admin" role request kar sakta hai
                         * - Security vulnerability
                         *
                         * Proper approach:
                         * - Admin role sirf super admin assign kar sake
                         * - Ya separate admin creation endpoint (protected)
                         * - Public signup pe only ROLE_USER allow karo
                         *
                         * FIX for production:
                         * if (requestIsFromAdminPanel && currentUserIsAdmin) {
                         *     // Allow admin role
                         * } else {
                         *     // Reject or ignore
                         * }
                         */
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(adminRole);
                        break;

                    case "seller":
                        /**
                         * SELLER ROLE ASSIGNMENT
                         *
                         * Use case:
                         * - E-commerce platform mein sellers register hote hain
                         * - Product listing rights chahiye
                         * - Order management access
                         *
                         * Permissions with ROLE_SELLER:
                         * - Add products
                         * - Manage inventory
                         * - View seller dashboard
                         * - Process orders
                         *
                         * Database fetch:
                         * - roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                         * - Returns seller Role entity from database
                         */
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(sellerRole);
                        break;

                    default:
                        /**
                         * DEFAULT CASE - Unknown/Invalid Role
                         *
                         * Jab user ne koi invalid role request kiya:
                         * - "moderator" (not in enum)
                         * - "superuser"
                         * - Typo: "adimn"
                         *
                         * Fallback behavior:
                         * - ROLE_USER assign karo (safe default)
                         * - User registration fail nahi hona chahiye
                         * - At least basic access de do
                         *
                         * Better approach:
                         * - Log warning: "Unknown role requested: {role}"
                         * - Notification to admin
                         * - Return error to client (strict validation)
                         */
                        Role userRoles = roleRepository.findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found"));
                        roles.add(userRoles);
                }
            });
        }

        // ====== STEP 6: USER KO ROLES ASSIGN KARO ======
        /**
         * User entity mein roles Set set karo
         *
         * user.setRole(roles):
         * - JPA relationship update
         * - Many-to-many mapping
         * - user_roles junction table mein entries jayengi
         *
         * Example database state:
         * users table:
         * | id | username | email          | password         |
         * | 1  | john_doe | john@email.com | $2a$10$abcd...  |
         *
         * roles table:
         * | id | role_name    |
         * | 1  | ROLE_USER    |
         * | 2  | ROLE_SELLER  |
         *
         * user_roles junction table (after save):
         * | user_id | role_id |
         * | 1       | 1       |
         * | 1       | 2       |
         */
        user.setRoles(roles);

        // ====== STEP 7: DATABASE MEIN USER SAVE KARO ======
        /**
         * User entity ko persist karo database mein
         *
         * userRepository.save(user):
         * - JPA save operation
         * - SQL: INSERT INTO users (username, email, password) VALUES (?, ?, ?)
         * - Cascade: user_roles table mein bhi entries insert
         * - Transaction: Atomically complete (all or nothing)
         *
         * After save:
         * - User ID auto-generate hota hai (@GeneratedValue)
         * - Roles properly linked ho jate hain
         * - User ab login kar sakta hai
         *
         * Possible exceptions:
         * - DataIntegrityViolationException: Constraint violation
         * - QueryTimeoutException: Database slow/down
         * - Should be handled in @ExceptionHandler
         */
        userRepository.save(user);

        // ====== STEP 8: SUCCESS RESPONSE RETURN KARO ======
        /**
         * Registration successful - 200 OK response
         *
         * ResponseEntity.ok():
         * - HTTP Status: 200 OK
         * - Indicates: Operation completed successfully
         *
         * MessageResponse:
         * - Success message DTO
         * - Frontend isse parse karke success notification show karega
         *
         * Response:
         * Status: 200
         * Body: { "message": "User registered successfully" }
         *
         * Frontend next steps:
         * 1. Show success message to user
         * 2. Redirect to login page
         * 3. Or auto-login the user (optional)
         * 4. Clear registration form
         *
         * Alternative response:
         * - Return user details (without password)
         * - Return auto-generated JWT token (immediate login)
         * - Return user ID for tracking
         */
        return ResponseEntity.ok(new MessageResponse("User registered successfully"));
    }

    @GetMapping("/username")
    public String currentUserName(Authentication authentication) {
        if (authentication != null) {
            return authentication.getName();
        } else {
            return " ";
        }
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse userInfoResponse = new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), roles);
        return ResponseEntity.ok().body(userInfoResponse);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You have been signed out !!!"));
    }
}
