package nl.uwv.otod.otod_portal.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static Logger logger = LogManager.getLogger();
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		logger.info("Configuring");
		http
			.authorizeRequests(authorize -> { authorize
					// public resources
					.antMatchers("/").permitAll()
					.antMatchers("/h2-console/**").permitAll()
					.antMatchers("/resources/js/**").permitAll()
					.antMatchers("/resources/webjars/**").permitAll()
					.antMatchers("/resources/css/**").permitAll()
					.antMatchers("/resources/images/**").permitAll()
					.antMatchers("/resources/pdf/**").permitAll()
					.antMatchers("/webjars/**").permitAll()
					.antMatchers("/subscription/**").permitAll()
					.antMatchers("/**/*.js", "/**/*.css").permitAll()

					.antMatchers("/user/prepareResetForgotPassword").permitAll()
					.antMatchers("/user/resetPassword").permitAll()
					.antMatchers("/forgot_password").permitAll()
					.antMatchers("/reset_password").permitAll()
					.antMatchers("/user/updatePassword").hasAnyAuthority("user")

					// logged in actions
					.antMatchers("/home").hasAnyAuthority("admin","user")
					.antMatchers("/request/editMine/**").hasAnyAuthority("admin","user")
					.antMatchers("/request/mine/**").hasAnyAuthority("admin","user")
					.antMatchers("/request/new/**").hasAnyAuthority("admin","user")
					
					// admin actions
					.mvcMatchers("/admin/**").hasAuthority("admin")
					.mvcMatchers("/fileSystem/**").hasAuthority("admin")
					.mvcMatchers("/middleware/**").hasAuthority("admin")
					.mvcMatchers("/os/**").hasAuthority("admin")
					.mvcMatchers("/availableIpAddresses/**").hasAuthority("admin")
					.mvcMatchers("/usedIpAddress/**").hasAuthority("admin")
					.mvcMatchers("/sizingRow/**").hasAuthority("admin")
					.mvcMatchers("/user/admin").hasAuthority("admin")
					.mvcMatchers("/user/edit/**").hasAuthority("admin")
					.mvcMatchers("/user/new").hasAuthority("admin")
					.mvcMatchers("/user/save").hasAuthority("admin")
					.mvcMatchers("/loginSuccess/**").hasAnyAuthority("admin", "user")
					.mvcMatchers("/loginFailure/**").hasAuthority("admin")
					.mvcMatchers("/calculation/**").hasAuthority("admin")
					.mvcMatchers("/project/**").hasAuthority("admin")
					.mvcMatchers("/server/**").hasAuthority("admin")
					.mvcMatchers("/request/edit/**").hasAuthority("admin")
					.mvcMatchers("/request/**").hasAuthority("admin")
					.mvcMatchers("/login").hasAnyAuthority("admin", "user")
					;}
					)
			.authorizeRequests()
			.anyRequest().authenticated()
			.and()
			
			.formLogin()
				.loginPage("/login")
				.permitAll()
			.and()
			.logout()
				.permitAll();
		//  registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	  
		http.headers()
			.xssProtection()
			.and()
			.frameOptions()
			.sameOrigin();
	
		http.formLogin()
			.defaultSuccessUrl("/", true);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.
		authenticationProvider(authenticationProvider());
	}
	
	@Bean
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}
	
	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		var authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		authenticationProvider.setUserDetailsService(userDetailsService);
		return authenticationProvider;
	};
	
	private PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
	    return new HttpSessionEventPublisher();
	}
}
