package cc.lnkd.urlshortener.models.response;

import cc.lnkd.urlshortener.models.RegisteredUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private RegisteredUser user;
    private String token;
}

