package it.uniroma3.siw.authentication;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private static final String ESPERTO_ROLE = "ROLE_ESPERTO";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // Assegna il ruolo ESPERTO a tutti gli utenti OAuth2
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(ESPERTO_ROLE)),
                oAuth2User.getAttributes(),
                "nome"
        );
    }
}
