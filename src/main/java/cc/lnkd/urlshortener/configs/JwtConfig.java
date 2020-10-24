package cc.lnkd.urlshortener.configs;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
}
