package cc.lnkd.urlshortener.lnkdBeanBasket;

import cc.lnkd.urlshortener.db.DBConfig;
import cc.lnkd.urlshortener.jwt.JwtConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanBasket {

    /*
    * PLEASE DO NOT DELETE, THIS BEANS ARE AUTO INJECTED AND ARE NOT CALLED DIRECTLY
    * SIGNED: BOBBY
    * */

    //This creates a new instance of DBConfig available for "Autowiring"/injection anywhere in the project
    @Bean
    public DBConfig getDBConfig(){
        return new DBConfig();
    }

    @Bean
    public JwtConfig getJwtConfig(){
        return new JwtConfig();
    }

    @Bean
    public PasswordEncoder getPassword(){
        //10 is the strength of the password encoder
        return new BCryptPasswordEncoder(10);
    }

    //ObjectMapper already has a bean provided by Spring boot, no need for custom bean

}
