package com.ecommerce.sbecom.security.services;

import com.ecommerce.sbecom.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDetailsImpl implements UserDetails {
    @Serial
    private static final long serialVersionUID = 1L;
    Collection<? extends GrantedAuthority> authorities;
    private Long id;
    private String username;
    private String email;
    private String password;

    public UserDetailsImpl(Long id, String username, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }


    public static UserDetailsImpl build(User user) {
        // Step 1: User ke roles ko GrantedAuthority list mein convert karo
        List<GrantedAuthority> authorities = user.getRoles()// Returns: List<Role> (from database)
                .stream()// Stream banao
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());
        // Step 2: New UserDetailsImpl object banao with copied data
        return new UserDetailsImpl(
                user.getUserId(), // 1L
                user.getUserName(), // "rahul_kumar"
                user.getEmail(), // "rahul@example.com"
                user.getPassword(),  // "$2a$10$abc123..."
                authorities // [ROLE_CUSTOMER, ROLE_USER]
        );
    }
    // ================================== OR=================================
    //private final User user;
//    public UserDetailsImpl(User user) {
//        this.user = user;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UserDetailsImpl user = (UserDetailsImpl) obj;
        return Objects.equals(id, user.id);
    }
}
