package cc.lnkd.urlshortener.jwt;

import cc.lnkd.urlshortener.auth.LnkdUserDetails;
import cc.lnkd.urlshortener.auth.LnkdUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
    @Autowired
    private LnkdUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    ObjectMapper mapper;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        System.out.println("Remote IP Address: "+request.getRemoteAddr());

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            if(jwtUtil.isTokenExpired(jwt)){
                sendInvalidTokenResponse(response); //Sends error response to client/user
                return; //Breaks from the filter chain, to prevent the request from reaching controller
            }else {
                username = jwtUtil.extractUsername(jwt);
            }
        }


        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            LnkdUserDetails userDetails = (LnkdUserDetails) this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

    private void sendInvalidTokenResponse(HttpServletResponse response) throws IOException {
        Map<String, Object> errorDetails = new LinkedHashMap<>();
        errorDetails.put("status", false);
        errorDetails.put("message", "Invalid access token");
        errorDetails.put("data", null);

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), errorDetails); //Sends error response to client/user
    }
}
