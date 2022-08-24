package stage.agencedirectserver.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@Slf4j
public class GenerateTokenUtil {
    public static void generateToken(HttpServletRequest request, HttpServletResponse response,Agent agent,String refreshToken,Algorithm algorithm) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : agent.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String accessToken = JWT.create()
                        .withSubject(agent.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000))//expire in 3 days
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", agent.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,Object> tokens =new HashMap<>();
                tokens.put("accessToken",accessToken);
                tokens.put("refreshToken",refreshToken);
                tokens.put("roles",authorities);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception e) {
                log.error("error wrong token");
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error-Message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }

        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}
