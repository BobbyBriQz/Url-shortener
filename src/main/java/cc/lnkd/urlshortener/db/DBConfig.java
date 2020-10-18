package cc.lnkd.urlshortener.db;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.datasource")
public class DBConfig {

    private String url;
    private String username;
    private String password;

}
