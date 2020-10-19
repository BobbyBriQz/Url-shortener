package cc.lnkd.urlshortener.models.response;

import cc.lnkd.urlshortener.models.RegisteredUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationResponse {

    private RegisteredUser user;
    private String token;
}
