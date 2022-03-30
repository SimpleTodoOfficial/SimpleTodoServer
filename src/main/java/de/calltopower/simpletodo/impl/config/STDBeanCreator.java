package de.calltopower.simpletodo.impl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.calltopower.simpletodo.api.config.STDConfig;
import de.calltopower.simpletodo.impl.filter.STDAuthTokenFilter;

@Configuration
public class STDBeanCreator implements STDConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public STDAuthTokenFilter authenticationJwtTokenFilter() {
		return new STDAuthTokenFilter();
	}

}
