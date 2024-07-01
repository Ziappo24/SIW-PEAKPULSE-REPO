package it.uniroma3.siw.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import it.uniroma3.siw.model.Credentials;

import javax.sql.DataSource;

import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
import static it.uniroma3.siw.model.Credentials.ESPERTO_ROLE;

import java.util.Collections;
import java.util.UUID;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

	@Autowired
	private DataSource dataSource;

	// Configura come il sistema deve recuperare username, password e ruoli nel DB
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
				.usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
	}

	// Definisce come salvare in maniera criptata la password nel database
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// Configura la sicurezza HTTP
	@Bean
	protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.csrf().disable().authorizeHttpRequests(auth -> {
			auth.requestMatchers(HttpMethod.GET, "/", "/index", "/register", "/css/**", "/images/**", "/favicon.ico",
					"/esperti", "/listaAttivita", "/attrezzature", "/esperto/**", "/attivita/**", "/attrezzatura/**",
					"/recensione/**", "/recensioni/**").permitAll();
			auth.requestMatchers(HttpMethod.POST, "/register", "/login", "/searchAttivita", "/searchEsperti", "/searchRecensioni", "/searchAttrezzature").permitAll();
			auth.requestMatchers(HttpMethod.GET, "/esperto/**").hasAnyAuthority(ESPERTO_ROLE);
			auth.requestMatchers(HttpMethod.POST, "/esperto/**").hasAnyAuthority(ESPERTO_ROLE);
			auth.requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_ROLE);
			auth.requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE);
			auth.anyRequest().authenticated();
		}).oauth2Login(oauth2 -> oauth2.loginPage("/login").defaultSuccessUrl("/success")
				.failureUrl("/login/error")

		).formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/success").failureUrl("/login/error")
				.permitAll())
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/").invalidateHttpSession(true)
						.deleteCookies("JSESSIONID").logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.clearAuthentication(true).permitAll());
		return httpSecurity.build();
	}
}
