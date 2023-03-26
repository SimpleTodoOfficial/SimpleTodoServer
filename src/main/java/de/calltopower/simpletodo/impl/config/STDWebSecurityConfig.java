package de.calltopower.simpletodo.impl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import de.calltopower.simpletodo.api.config.STDConfig;
import de.calltopower.simpletodo.impl.controller.STDConnectionController;
import de.calltopower.simpletodo.impl.controller.STDI18nController;
import de.calltopower.simpletodo.impl.controller.STDUserController;
import de.calltopower.simpletodo.impl.exception.STDAuthEntryPointJwt;
import de.calltopower.simpletodo.impl.filter.STDAuthTokenFilter;

/**
 * Web security configuration
 */
@Configuration
@EnableWebSecurity
// @formatter:off
@EnableMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true
)
// @formatter:on
public class STDWebSecurityConfig implements STDConfig {

    // @formatter:off
    private static final AntPathRequestMatcher[] AUTH_WHITELIST = {
        new AntPathRequestMatcher("/api/auth/**"),
        new AntPathRequestMatcher(String.format("%s/password/forgot", STDUserController.PATH)),
        new AntPathRequestMatcher(String.format("%s/password/reset/**", STDUserController.PATH)),
        new AntPathRequestMatcher(String.format("%s/activate/**", STDConnectionController.PATH)),
        new AntPathRequestMatcher(String.format("%s/available", STDConnectionController.PATH)),
        new AntPathRequestMatcher(String.format("%s/**", STDI18nController.PATH))
    };
    // @formatter:on

    private STDAuthTokenFilter authTokenFilter;
    private STDAuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public STDWebSecurityConfig(STDAuthTokenFilter authTokenFilter, STDAuthEntryPointJwt unauthorizedHandler) {
        this.authTokenFilter = authTokenFilter;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        httpSecurity
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(handler -> handler.authenticationEntryPoint(unauthorizedHandler))
            .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic(Customizer.withDefaults());
        // @formatter:on

        return httpSecurity.build();
    }

}
