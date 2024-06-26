package it.uniroma3.siw.model;

import java.util.Collection;
import java.util.Random;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class CustomUserDetails extends Credentials {

    public CustomUserDetails(Long id, String username, String password, String ruolo, User user) {
        super(id, username, password, ruolo, user);
    }

    // Metodo statico per convertire un DefaultOidcUser in CustomUserDetails
    public static CustomUserDetails fromOidcUser(DefaultOidcUser oidcUser) {
        // Estrarre le informazioni necessarie dall'oggetto DefaultOidcUser
        String username = oidcUser.getFullName();
        // Supponendo che il ruolo "ESPERTO" sia fisso
        String ruolo = "ROLE_ESPERTO";  // Ruolo fisso per gli utenti Google
        Random random = new Random();
         
        Long id = random.nextLong();  // Assegna un id adeguato, se disponibile
        User user = new User();  // Assegna l'utente adeguato, se disponibile
        
        // Creazione delle authorities
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(ruolo);

        return new CustomUserDetails(id, username, "", ruolo, user);
    }
    
    public static CustomUserDetails fromUserDetails(UserDetails userDetails) {
        // Estrarre le informazioni necessarie dall'oggetto UserDetails
        String username = userDetails.getUsername();
        // Supponendo che il ruolo "ESPERTO" sia fisso
        String ruolo = "ROLE_ESPERTO";  // Ruolo fisso per gli utenti registrati
        Long id = null;  // Assegna un id adeguato, se disponibile
        User user = null;  // Assegna l'utente adeguato, se disponibile

        // Creazione delle authorities
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        return new CustomUserDetails(id, username, "", ruolo, user);
    }
    
    public static CustomUserDetails fromOAuth2User(DefaultOAuth2User oauth2User) {
        // Estrarre le informazioni necessarie dall'oggetto DefaultOAuth2User
        String username = oauth2User.getAttribute("email");  // Sostituisci con l'attributo corretto che contiene l'email
        String ruolo = "ROLE_ESPERTO";  // Ruolo fisso per gli utenti registrati
        Long id = null;  // Assegna un id adeguato, se disponibile
        User user = null;  // Assegna l'utente adeguato, se disponibile

        // Creazione delle authorities
        Collection<? extends GrantedAuthority> authorities = oauth2User.getAuthorities();

        return new CustomUserDetails(id, username, "", ruolo, user);
    }
}
