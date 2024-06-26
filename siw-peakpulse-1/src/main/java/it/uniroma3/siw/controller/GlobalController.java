package it.uniroma3.siw.controller;

import java.util.Collection;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import it.uniroma3.siw.model.CustomUserDetails;

@ControllerAdvice
public class GlobalController {

	@ModelAttribute("userDetails")
	public Object getUser() {
	    Object user = null;

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    if (!(authentication instanceof AnonymousAuthenticationToken)) {
	        Object principal = authentication.getPrincipal();
	        if (principal instanceof UserDetails) {
	            user = (UserDetails) principal;
	        } else if (principal instanceof DefaultOidcUser) {
	            user = CustomUserDetails.fromOidcUser((DefaultOidcUser) principal);
	        } else if (principal instanceof DefaultOAuth2User) {
	            user = CustomUserDetails.fromOAuth2User((DefaultOAuth2User) principal);
	        } else if (principal instanceof OAuth2User) {
	            user = CustomUserDetails.fromOAuth2User(new DefaultOAuth2User((Collection<? extends GrantedAuthority>) ((OAuth2User) principal).getAuthorities(), ((OAuth2User) principal).getAttributes(), ((OAuth2User) principal).getName()));
	        }
	    }
	    
	    return user;
	}
}
