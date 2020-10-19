package cc.lnkd.urlshortener.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements AuthRequest{

    private String email;
    private String password;
}
