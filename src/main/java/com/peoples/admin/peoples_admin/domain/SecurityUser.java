package com.peoples.admin.peoples_admin.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@EqualsAndHashCode
public class SecurityUser implements UserDetails {
    @EqualsAndHashCode.Exclude
    private Admin admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authority= new HashSet<>();
        authority.add(new SimpleGrantedAuthority(this.admin.getRole().toString()));
        return authority;
    }

    @Override
    public String getPassword() {
        return this.admin.getPassword();
    }

    @Override
    @EqualsAndHashCode.Include
    public String getUsername() {
        return this.admin.getUserId();
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

    @Builder
    public SecurityUser(Admin admin) {
        this.admin = admin;
    }

    public static SecurityUser of(Admin admin) {
        return SecurityUser.builder().admin(admin).build();
    }

}
