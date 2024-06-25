package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Esperto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.EspertoService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {

//	private static String UPLOAD_DIR = "C:\\Users\\EDOARDO\\Desktop\\FOR SISW\\siw-peakpulse-repo\\siw-peakpulse-1\\src\\main\\resources\\static\\images";
	private static String UPLOAD_DIR = "C:\\Users\\utente\\Desktop\\UNIR3\\TERZO ANNO\\II SEMESTRE\\SISW\\siw-peakpulse-repo\\siw-peakpulse-1\\src\\main\\resources\\static\\images";
//	private static String UPLOAD_DIR = "C:\\Users\\UTENTE\\Documents\\workspace-spring-tool-suite-4-4.22.0.RELEASE\\siw-peakpulse-repo\\siw-peakpulse-1\\src\\main\\resources\\static\\images";

	@Autowired
	private CredentialsService credentialsService;

	@Autowired
	private UserService userService;

	@Autowired
	private EspertoService espertoService;

	@GetMapping(value = "/register")
	public String showRegisterForm(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}

	@GetMapping(value = "/login")
	public String showLoginForm(Model model) {
		return "formLogin.html";
	}

	@GetMapping(value = "/login/error")
	public String showLoginErrorForm(Model model) {
		String messaggioErrore = new String("Username o password incorretti");
		model.addAttribute("messaggioErrore", messaggioErrore);
		return "formLogin.html";
	}

	@GetMapping("/")
	public String index(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
	    } else {
	        if (authentication.getPrincipal() instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	                model.addAttribute("userDetails", userDetails);
	                return "admin/indexAdmin.html";
	            } else if (credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
	                model.addAttribute("userDetails", userDetails);
	                return "esperto/indexEsperto.html";
	            }
	        } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
	            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
	            Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities();
	            Set<String> authoritySet = authorities.stream()
	                    .map(GrantedAuthority::getAuthority)
	                    .collect(Collectors.toSet());

	            // Controlla se l'utente ha il ruolo ESPERTO_ROLE e aggiungilo se non è presente
	            if (!authoritySet.contains("ESPERTO_ROLE")) {
	                authoritySet.add("ESPERTO_ROLE");
	            }

	            model.addAttribute("userDetails", oidcUser);

	            if (authoritySet.contains("ADMIN_ROLE")) {
	                return "admin/indexAdmin.html";
	            } else if (authoritySet.contains("ESPERTO_ROLE")) {
	                return "esperto/indexEsperto.html";
	            }
	        }
	    }

	    return "index.html";
	}


	@GetMapping("/success")
	public String defaultAfterLogin(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication != null && authentication.isAuthenticated()) {
	        if (authentication.getPrincipal() instanceof UserDetails) {
	            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	            Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());

	            if (!credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
	                credentials.setRole(Credentials.ESPERTO_ROLE); // Assicurati che le credenziali abbiano ESPERTO_ROLE
	            }

	            model.addAttribute("userDetails", userDetails);

	            if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	                return "admin/indexAdmin.html";
	            } else if (credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
	                return "esperto/indexEsperto.html";
	            }
	        } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
	            DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
	            Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities();
	            Set<String> authoritySet = authorities.stream()
	                    .map(GrantedAuthority::getAuthority)
	                    .collect(Collectors.toSet());

	            // Controlla se l'utente ha il ruolo ESPERTO_ROLE e aggiungilo se non è presente
	            if (!authoritySet.contains("ESPERTO_ROLE")) {
	                authoritySet.add("ESPERTO_ROLE");
	            }

	            model.addAttribute("userDetails", oidcUser);

	            if (authoritySet.contains("ADMIN_ROLE")) {
	                return "admin/indexAdmin.html";
	            } else if (authoritySet.contains("ESPERTO_ROLE")) {
	                return "esperto/indexEsperto.html";
	            }
	        }
	    }

	    return "index.html";
	}

//	@GetMapping(value = "/") 
//	public String index(Model model) {
//		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		if (authentication instanceof AnonymousAuthenticationToken) {
//	        return "index.html";
//		}
//		else {		
//			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
//			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//				return "admin/indexAdmin.html";
//			}
//			if (credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
//				return "esperto/indexEsperto.html";
//			}
//		}
//        return "index.html";
//	}
//		
//    @GetMapping(value = "/success")
//    public String defaultAfterLogin(Model model) {
//        
//    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
//    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
//            return "admin/indexAdmin.html";
//        }
//    	if (credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
//            return "esperto/indexEsperto.html";
//        }
//        return "index.html";
//    }

	@PostMapping(value = { "/register" })
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult,
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credentialsBindingResult,
			@RequestParam("immagine") MultipartFile file, Model model) {

		// se user e credential hanno entrambi contenuti validi, memorizza User e the
		// Credentials nel DB
		if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
			if (!file.isEmpty()) {
				try {
					// Salva il file sul server
					String fileName = StringUtils.cleanPath(file.getOriginalFilename());
					Path path = Paths.get(UPLOAD_DIR + File.separator + fileName);
					Files.write(path, file.getBytes());
					user.setUrlImage(fileName); // Assumi che l'entità User abbia un campo urlImage
				} catch (IOException e) {
					e.printStackTrace();
					model.addAttribute("errorMessage", "Errore durante il salvataggio dell'immagine");
					return "registrationForm";
				}
			} else {
				model.addAttribute("errorMessage", "Il file dell'immagine è vuoto");
				return "registrationForm";
			}

			// Salva l'utente e le credenziali
			userService.saveUser(user);
			credentials.setUser(user);
			credentialsService.saveCredentials(credentials);

			// Crea e salva un nuovo Esperto
			Esperto nuovoEsperto = new Esperto();
			nuovoEsperto.setNome(user.getNome());
			nuovoEsperto.setCognome(user.getCognome());
			nuovoEsperto.setNascita(user.getNascita());
			nuovoEsperto.setUrlImage(user.getUrlImage());
			nuovoEsperto.setDescrizione(user.getDescrizione());
			espertoService.save(nuovoEsperto);

			model.addAttribute("user", user);
			return "registrationSuccessful";
		} else {
			return "registrationForm";
		}
	}

	/*
	 * @GetMapping("/success") public String getUserInfo(Authentication
	 * authentication, Model model) { // Variabile per il ruolo String role = null;
	 * 
	 * // Controlla se l'utente è autenticato tramite OAuth2 if
	 * (authentication.getPrincipal() instanceof OAuth2User) { OAuth2User oauth2User
	 * = (OAuth2User) authentication.getPrincipal();
	 * 
	 * String fullName = oauth2User.getAttribute("name");
	 * 
	 * // Dividi la stringa in base allo spazio usando split(" ") String[] parts =
	 * fullName.split(" ");
	 * 
	 * // Ora hai un array con due elementi: parts[0] contiene il nome, parts[1] //
	 * contiene il cognome String nome = parts[0]; // "Edoardo" String cognome =
	 * parts[1]; // "Piazzolla"
	 * 
	 * // Ottieni le informazioni dell'utente da OAuth2 String username =
	 * oauth2User.getAttribute("given_name"); String email =
	 * oauth2User.getAttribute("email"); String imageUrl =
	 * oauth2User.getAttribute("picture");
	 * 
	 * // Aggiungi logica per determinare il ruolo ESPERTO_ROLE role =
	 * determineRoleFromOAuth2User(oauth2User);
	 * 
	 * // Verifica se l'utente esiste già nel sistema User existingUser =
	 * userService.findUserByNomeAndCognome(nome, cognome); if (existingUser ==
	 * null) { // Se l'utente non esiste, crea un nuovo utente e assegna il ruolo
	 * ESPERTO_ROLE User newUser = new User(); newUser.setNome(nome);
	 * newUser.setCognome(cognome); newUser.setEmail(email);
	 * newUser.setUrlImage(imageUrl); userService.saveUser(newUser);
	 * 
	 * // Crea le credenziali per il nuovo utente Credentials newCredentials = new
	 * Credentials(); newCredentials.setUsername(username); int passwordLength = 10;
	 * // Lunghezza della password desiderata SecureRandom secureRandom = new
	 * SecureRandom(); byte[] randomBytes = new byte[passwordLength];
	 * secureRandom.nextBytes(randomBytes);
	 * newCredentials.setPassword(Base64.getUrlEncoder().withoutPadding().
	 * encodeToString(randomBytes).substring(0, passwordLength)); // Genera una
	 * password casuale newCredentials.setRole(role); // Assegna il ruolo
	 * ESPERTO_ROLE credentialsService.saveCredentials(newCredentials); } } else if
	 * (authentication.getPrincipal() instanceof UserDetails) { // Se l'utente è
	 * autenticato tramite il meccanismo standard di Spring Security UserDetails
	 * userDetails = (UserDetails) authentication.getPrincipal(); Credentials
	 * credentials = credentialsService.getCredentials(userDetails.getUsername());
	 * if (credentials != null) { role = credentials.getRole(); } }
	 * 
	 * // Reindirizza in base al ruolo if (Credentials.ADMIN_ROLE.equals(role)) {
	 * return "admin/indexAdmin.html"; // Reindirizza a indexAdmin se l'utente è un
	 * admin } else if (Credentials.ESPERTO_ROLE.equals(role)) { return
	 * "esperto/indexEsperto.html"; // Reindirizza a indexEsperto se l'utente è un
	 * esperto } return "index.html"; }
	 */
}
