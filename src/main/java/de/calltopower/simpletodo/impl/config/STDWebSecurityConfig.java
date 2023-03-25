package de.calltopower.simpletodo.impl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.calltopower.simpletodo.api.config.STDConfig;
import de.calltopower.simpletodo.impl.controller.STDConnectionController;
import de.calltopower.simpletodo.impl.controller.STDI18nController;
import de.calltopower.simpletodo.impl.controller.STDUserController;
import de.calltopower.simpletodo.impl.exception.STDAuthEntryPointJwt;
import de.calltopower.simpletodo.impl.filter.STDAuthTokenFilter;
import de.calltopower.simpletodo.impl.service.STDUserDetailsService;

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

    private PasswordEncoder passwordEncoder;
    private STDAuthTokenFilter authTokenFilter;
    private STDUserDetailsService userDetailsService;
    private STDAuthEntryPointJwt unauthorizedHandler;

    @Autowired
    public STDWebSecurityConfig(PasswordEncoder passwordEncoder, STDAuthTokenFilter authTokenFilter,
            STDUserDetailsService userDetailsService, STDAuthEntryPointJwt unauthorizedHandler) {
        this.passwordEncoder = passwordEncoder;
        this.authTokenFilter = authTokenFilter;
        this.userDetailsService = userDetailsService;
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
        .cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
        .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and().authorizeHttpRequests()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers(String.format("%s/password/forgot", STDUserController.PATH)).permitAll()
            .requestMatchers(String.format("%s/password/reset/**", STDUserController.PATH)).permitAll()
            .requestMatchers(String.format("%s/activate/**", STDConnectionController.PATH)).permitAll()
            .requestMatchers(String.format("%s/available", STDConnectionController.PATH)).permitAll()
            .requestMatchers(String.format("%s/**", STDI18nController.PATH)).permitAll()
            .anyRequest().authenticated();
        // @formatter:on

        httpSecurity.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

}
