package cc.lnkd.urlshortener.auth;

import cc.lnkd.urlshortener.models.AuthUser;
import cc.lnkd.urlshortener.services.UserService;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LnkdUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;


    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        AuthUser authUser = userService.getAuthUserByEmail(email);

        if(authUser == null){
            throw new UsernameNotFoundException(email);
        }

        return new LnkdUserDetails(authUser);
    }
}
