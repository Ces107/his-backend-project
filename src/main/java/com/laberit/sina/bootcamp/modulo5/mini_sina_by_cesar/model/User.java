package com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model;

import com.laberit.sina.bootcamp.modulo5.mini_sina_by_cesar.model.enumerations.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission")
    private Set<String> permissions;

    public User(String firstName, String lastName, String username, String encryptedPassword, UserRole role, Set<String> permissions) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = encryptedPassword;
        this.role = role;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Combine role-based authorities and permissions
        var roleAuthorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        var permissionAuthorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
        return new ArrayList<>(List.copyOf(roleAuthorities));
    }

    @Override
    public String getUsername() {
        return this.username;
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
}
