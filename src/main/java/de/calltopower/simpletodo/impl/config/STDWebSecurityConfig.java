package de.calltopower.simpletodo.impl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import de.calltopower.simpletodo.api.config.STDConfig;
import de.calltopower.simpletodo.impl.controller.STDConnectionController;
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
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true
)
// @formatter:on
public class STDWebSecurityConfig extends WebSecurityConfigurerAdapter implements STDConfig {

    @Autowired
    private STDUserDetailsService userDetailsService;

    @Autowired
    private STDAuthEntryPointJwt unauthorizedHandler;

    @Bean
    public STDAuthTokenFilter authenticationJwtTokenFilter() {
        return new STDAuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // @formatter:off
        httpSecurity
            .cors().and().csrf().disable()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
            .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers(String.format("%s/password/forgot", STDUserController.PATH)).permitAll()
                .antMatchers(String.format("%s/password/reset/**", STDUserController.PATH)).permitAll()
                .antMatchers(String.format("%s/activate/**", STDConnectionController.PATH)).permitAll()
                .antMatchers(String.format("%s/available", STDConnectionController.PATH)).permitAll()
                .anyRequest().authenticated();
        // @formatter:on

        httpSecurity.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

}
