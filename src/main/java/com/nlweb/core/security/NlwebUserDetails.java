package com.nlweb.core.security;

import com.nlweb.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class NlwebUserDetails  implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (user.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public UUID getUserId() {
        return user.getId();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName() {
        return user.getFullName();
    }

    public Integer getBatch() {
        return user.getBatch();
    }

    public String getSession() {
        return user.getSession().getDescription();
    }

    public Boolean isVocalAllowed() {
        return user.getIsVocalAllowed();
    }

    public boolean isAdmin() {
        return user.isAdmin();
    }

    public String toString() {
        return String.format("NlwebUserDetails{username = '%s', authorities = %s}",
                user.getUsername(), getAuthorities());
    }
}
