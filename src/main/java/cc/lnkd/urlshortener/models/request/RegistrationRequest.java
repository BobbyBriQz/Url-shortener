package cc.lnkd.urlshortener.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest implements AuthRequest{

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
