package de.calltopower.simpletodo.impl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import de.calltopower.simpletodo.api.config.STDConfig;
import de.calltopower.simpletodo.impl.service.STDUserDetailsService;

public class STDGlobalConfig implements STDConfig {

	private PasswordEncoder passwordEncoder;
	private STDUserDetailsService userDetailsService;

	@Autowired
	public STDGlobalConfig(PasswordEncoder passwordEncoder, STDUserDetailsService userDetailsService) {
		this.passwordEncoder = passwordEncoder;
		this.userDetailsService = userDetailsService;
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}

}
