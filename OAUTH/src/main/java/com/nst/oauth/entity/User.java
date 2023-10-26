package com.nst.oauth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Table(name = "USER_TABLE")
public class User implements UserDetails {
    @Id
    @NonNull
    private String id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Arrays.stream(roles.split(",")).toList();
        return Arrays.stream(roles.split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
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

}
