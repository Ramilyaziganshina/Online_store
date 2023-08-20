package ru.skypro.homework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.service.UserService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@EnableMethodSecurity(prePostEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends GlobalMethodSecurityConfiguration {

    private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
//    private final UserService userService;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/ads",
            "/images/**"
    };

//    public WebSecurityConfig(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        return new InMemoryUserDetailsManager(userService.getUserDetails());
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Processing: " + http.toString());
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        (authorization) ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
