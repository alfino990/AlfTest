package com.pccube;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.pccube.entities.UserRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceIMP userDetailsServiceIMP;

	@Autowired
	private SimpleAuthenticationSuccessHandler successHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		//.antMatchers("/taskAdmin").hasAuthority("admin") per evitare che si acceda a delle pagine direttamente dall'url
		http.authorizeRequests().antMatchers("/", "/home").permitAll()
				.antMatchers("/taskAdmin").hasAuthority("admin")
				.antMatchers("/taskUser").hasAuthority("user")
				.anyRequest().authenticated().and().formLogin().successHandler(successHandler)
				.loginPage("/login").usernameParameter("username").passwordParameter("password")

				.permitAll().and().logout().permitAll();
		http.csrf().disable();
		http.headers().frameOptions().disable();
	}

	@Override
	public void configure(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(userDetailsServiceIMP)

				.passwordEncoder(passwordEncoder());

	}

	@Bean
	public UserDetailsServiceIMP UserDetailsService() {
		return new UserDetailsServiceIMP();
	};

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}