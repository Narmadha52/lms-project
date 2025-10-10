package com.lms.service;

import com.lms.model.User;
import com.lms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import com.lms.model.Role; // Assuming Role is imported

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username));

        // CRITICAL: Returns the object that implements UserDetails
        return UserPrincipal.create(user);
    }

    // --- CRITICAL MISSING CLASS: UserPrincipal (implements UserDetails) ---
    public static class UserPrincipal implements UserDetails {
        private Long id;
        private String username;
        private String email;
        private String password;
        private String firstName;
        private String lastName;
        private Role role;
        private Collection<? extends GrantedAuthority> authorities;
        private Boolean isApproved;

        public UserPrincipal(Long id, String username, String email, String password,
                             String firstName, String lastName, Role role,
                             Collection<? extends GrantedAuthority> authorities, Boolean isApproved) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.password = password;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.authorities = authorities;
            this.isApproved = isApproved;
        }

        public static UserPrincipal create(User user) {
            Collection<GrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            );

            return new UserPrincipal(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getRole(),
                    authorities,
                    user.getIsApproved()
            );
        }

        // --- Core UserDetails Methods ---
        @Override
        public String getUsername() { return username; }
        @Override
        public String getPassword() { return password; }
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
        @Override
        public boolean isEnabled() { return isApproved != null && isApproved; }
        @Override
        public boolean isAccountNonExpired() { return true; }
        @Override
        public boolean isAccountNonLocked() { return true; }
        @Override
        public boolean isCredentialsNonExpired() { return true; }

        // --- JWT Payload Getters ---
        public Long getId() { return id; }
        public String getEmail() { return email; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public Role getRole() { return role; }
        public Boolean getIsApproved() { return isApproved; }
    }
}