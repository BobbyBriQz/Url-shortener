package cc.lnkd.urlshortener.models;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@Builder
public class AuthUser {

    private String email;
    private String password;
    private boolean isEnabled;
    private List<String> roles;
}
