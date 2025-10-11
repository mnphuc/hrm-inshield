package vn.ts.insight.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import vn.ts.insight.domain.user.Role;
import vn.ts.insight.domain.user.UserAccount;

public class CustomUserDetails implements UserDetails {

    private final UserAccount account;
    private final Set<GrantedAuthority> authorities;

    public CustomUserDetails(UserAccount account) {
        this.account = account;
        this.authorities = account.getRoles().stream()
            .map(Role::getName)
            .map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName.name()))
            .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return account.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return account.isEnabled();
    }
}
