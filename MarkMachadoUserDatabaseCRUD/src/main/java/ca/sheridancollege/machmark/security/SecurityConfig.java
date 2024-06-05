package ca.sheridancollege.machmark.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private LoggingAccessDeniedHandler accessDeniedHandler;


	// This handles all the security of the website
	@Bean
	public SecurityFilterChain securityfilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
			throws Exception {
		MvcRequestMatcher.Builder mvc = new MvcRequestMatcher.Builder(introspector);
		return http.authorizeHttpRequests(authorize -> authorize
				//The user first enters into the index
				.requestMatchers(mvc.pattern("/")).permitAll()
				.requestMatchers(mvc.pattern("/api/v1/testObject")).permitAll()
				.requestMatchers(mvc.pattern("/api/v1/testObject/**")).permitAll()
				// The user has to be authorized AND it has to have the role admin or user to
				// access secure index and pick poll
				.requestMatchers(mvc.pattern("/secure")).hasAnyRole("ADMIN", "USER")
				.requestMatchers(mvc.pattern("/secure/chooseTestObject")).hasAnyRole("ADMIN", "USER")
				.requestMatchers(mvc.pattern("/secure/updateObject")).hasAnyRole("ADMIN", "USER")
				.requestMatchers(mvc.pattern("/secure/updateObject/**")).hasAnyRole("ADMIN", "USER")
				.requestMatchers(mvc.pattern("/secure/pickTestObject")).hasAnyRole("ADMIN", "USER")
				// Here it only shows the ADMIN can access addPoll and deletePoll
				.requestMatchers(mvc.pattern("/secure/addTestObject")).hasRole("ADMIN")
				.requestMatchers(mvc.pattern("/secure/addTestObjectMethod")).hasRole("ADMIN")
				.requestMatchers(mvc.pattern("/secure/deleteTestObject")).hasRole("ADMIN")
				.requestMatchers(mvc.pattern("/secure/deleteTestObjectMethod")).hasRole("ADMIN")
				// Everything below anyone can access, this includes the index and all styles
				// along with register
				.requestMatchers(mvc.pattern("/fragments")).permitAll()
				.requestMatchers(mvc.pattern("/js/**")).permitAll().requestMatchers(mvc.pattern("/css/**")).permitAll()
				.requestMatchers(mvc.pattern("/images/**")).permitAll()
				.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST, "/register")).permitAll()
				.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/register")).permitAll()
				.requestMatchers(mvc.pattern("/permission-denied")).permitAll()
				.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
				.requestMatchers(mvc.pattern("/**")).denyAll())
				.csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")) 
						.disable())
				.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
				.formLogin(form -> form.loginPage("/login").permitAll())
				.exceptionHandling(exception -> exception.accessDeniedPage("/permission-denied"))
				.logout(logout -> logout.permitAll()).build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
