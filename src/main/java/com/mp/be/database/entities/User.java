/// File is generated from https://studio.fabbuilder.com - 
package com.mp.be.database.entities;

import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "mp_users")
public class User implements UserDetails {

    @Id
    public String id ;
    private String email;
    private String password;

    private List<TenantUser> tenants;
    private Boolean emailVerified = false;

    private String passwordResetToken;
    private Instant passwordResetTokenExpiresAt;
    private String emailVerificationToken;
    private Instant emailVerificationTokenExpiresAt;

    public User(String id) {
       this.id = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // should be tenant roles
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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