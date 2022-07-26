package stage.agencedirectserver.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import stage.agencedirectserver.filter.CustomAuthentificationFilter;
import stage.agencedirectserver.filter.CustomAuthorizationFilter;
import stage.agencedirectserver.services.CustomUserDetailsService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration @EnableWebSecurity @RequiredArgsConstructor @CrossOrigin(origins = "*")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthentificationFilter customAuthentificationFilter = new CustomAuthentificationFilter(authenticationManagerBean());
        customAuthentificationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        http.cors();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/login/**","/api/agent/token/refresh","/api/client/token/refresh").permitAll();
        /*http.authorizeRequests().antMatchers("/api/agent/**","/api/agent/token/refresh/**").hasAnyAuthority("ROLE_AGENT");
        http.authorizeRequests().antMatchers("/api/client/**","/api/agent/token/refresh/**").hasAnyAuthority("ROLE_CLIENT");
        http.authorizeRequests().antMatchers("/api/**").hasAnyAuthority("ROLE_ADMIN");*/
        http.authorizeRequests().anyRequest().permitAll();
        http.addFilter(customAuthentificationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource()
    {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000/");
        configuration.addAllowedOrigin("http://localhost:3001/");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
