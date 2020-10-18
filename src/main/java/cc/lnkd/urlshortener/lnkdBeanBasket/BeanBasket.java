package cc.lnkd.urlshortener.lnkdBeanBasket;

import cc.lnkd.urlshortener.db.DBConfig;
import cc.lnkd.urlshortener.repositories.LinkRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanBasket {

    //This creates a new instance of DBConfig available for "Autowiring"/injection anywhere in the project
    @Bean
    public DBConfig getDBConfig(){
        return new DBConfig();
    }

}
