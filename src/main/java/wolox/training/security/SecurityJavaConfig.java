package wolox.training.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import wolox.training.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
@SuppressWarnings("unused")
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    private static final String BOOKS_URL = "/api/books";
    private static final String USERS_URL = "/api/users";
    private static final String ALL_PATTERNS = "/**";

    @Autowired
    @SuppressWarnings("unused")
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider
            = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic()
            .and()
            .csrf().disable()
            .exceptionHandling()
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.GET, BOOKS_URL + ALL_PATTERNS).authenticated()
            .antMatchers(HttpMethod.GET, USERS_URL + ALL_PATTERNS).authenticated()
            .antMatchers(HttpMethod.DELETE, BOOKS_URL + ALL_PATTERNS).authenticated()
            .antMatchers(HttpMethod.DELETE, USERS_URL + ALL_PATTERNS).authenticated()
            .antMatchers(HttpMethod.PUT, BOOKS_URL + ALL_PATTERNS).authenticated()
            .antMatchers(HttpMethod.PUT, USERS_URL + ALL_PATTERNS).authenticated()
            .antMatchers(HttpMethod.POST, BOOKS_URL).permitAll()
            .antMatchers(HttpMethod.POST, USERS_URL).permitAll()
            .and()
            .formLogin()
            .and()
            .logout();
    }

}
