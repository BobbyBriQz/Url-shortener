package cc.lnkd.urlshortener.auth;

import cc.lnkd.urlshortener.models.AuthUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LnkdUserDetails implements UserDetails {

    private int userId;
    private String email;
    private String password;
    private boolean isEnabled;
    private List<GrantedAuthority> roles;

    public LnkdUserDetails(AuthUser authUser){
        this.email = authUser.getEmail();
        this.password = authUser.getPassword();
        this.isEnabled = authUser.isEnabled();
        this.roles = authUser.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        this.userId = authUser.getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public int getUserId(){ return userId;}

    @Override
    public String getUsername() {
        return email;
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
        return isEnabled;
    }
}
