package com.khahnm04.ecommerce.modules.auth.entity;

import com.khahnm04.ecommerce.modules.auth.enums.UserStatus;
import com.khahnm04.ecommerce.modules.auth.enums.Gender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true, length = 20)
    private String phoneNumber;

    @Column(name = "password", length = 500)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserProvider> userProviders;

    @Builder.Default
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    @Builder.Default
    @Column(name = "is_phone_verified")
    private Boolean isPhoneVerified = false;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Builder.Default
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.OTHER;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "login_ip", length = 50)
    private String loginIp;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @OrderBy("name ASC")
    private Set<Role> roles;

    @Override
    public String getUsername() {
        return this.phoneNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (roles == null)
            return List.of();
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != UserStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.status == UserStatus.ACTIVE;
    }

}
