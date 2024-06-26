package it.uniroma3.siw.authentication;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private CredentialsRepository cRepository;
	
	@Autowired
	private PasswordEncoder pEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
	    String username = authentication.getName();
	    String password = authentication.getCredentials().toString();
	    Credentials credentials = cRepository.findByUsername(username);

	    if (credentials != null) {
	        if (pEncoder.matches(password, credentials.getPassword())) {
	            List<GrantedAuthority> authorities = new ArrayList<>();
	            authorities.add(new SimpleGrantedAuthority(credentials.getRole()));
	            return new UsernamePasswordAuthenticationToken(username, password, authorities);
	        } else {
	            throw new BadCredentialsException("Invalid password");
	        }
	    } else {
	        throw new BadCredentialsException("No user registered with this username");
	    }
	}


	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
