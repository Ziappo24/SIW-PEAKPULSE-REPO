package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

	private static final String DESCRIZIONE_GOOGLE = "L'utente che esegue il login da google non possiede descrizione";
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
	public String index(Authentication authentication, Model model) {
	    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
	    } else if (authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	            return "/admin/indexAdmin.html";
	        } else if (credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
	            return "/esperto/indexEsperto.html";
	        }
	    } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
	        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
	        String email = oidcUser.getAttribute("email");

	        // Verifica se esiste un Credentials con questa email
	        Credentials existingCredentials = credentialsService.getCredentials(email);
	        if (existingCredentials != null) {
	            // Usa le credenziali esistenti
	            model.addAttribute("credentials", existingCredentials);
	            model.addAttribute("user", existingCredentials.getUser());

	            // Aggiungi il ruolo "ESPERTO" se non è già presente
	            if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ESPERTO"))) {
	                Collection<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
	                updatedAuthorities.add(new SimpleGrantedAuthority("ESPERTO"));
	                Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
	                SecurityContextHolder.getContext().setAuthentication(newAuth);
	            }

	            if (existingCredentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	                return "/admin/indexAdmin.html";
	            } else if (existingCredentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
	                return "/esperto/indexEsperto.html";
	            }
	        } else {
	            // Crea nuovo user e credentials
	            Credentials newCred = new Credentials();
	            newCred.setUsername(email);
	            newCred.setPassword(""); // OAuth2 non usa password locali
	            newCred.setRole("ESPERTO");

	            User newUser = new User();
	            String fullName = oidcUser.getAttribute("name");
	            String[] parts = fullName.split(" ");
	            String nome = parts[0];
	            String cognome = parts.length > 1 ? parts[1] : ""; // Evita l'ArrayIndexOutOfBoundsException
	            LocalDate birthday = oidcUser.getAttribute("birthday");

	            newUser.setNascita(birthday);
	            newUser.setNome(nome);
	            newUser.setCognome(cognome);
	            String urlImage = oidcUser.getAttribute("picture");
	            newUser.setUrlImage(urlImage);
	            newUser.setEmail(email);
	            newCred.setUser(newUser);

	            Esperto nuovoEsperto = new Esperto();
	            nuovoEsperto.setNome(newUser.getNome());
	            nuovoEsperto.setCognome(newUser.getCognome());
	            nuovoEsperto.setNascita(newUser.getNascita());
	            nuovoEsperto.setUrlImage(newUser.getUrlImage());
	            nuovoEsperto.setDescrizione(DESCRIZIONE_GOOGLE);
	            espertoService.save(nuovoEsperto);

	            // Salva il nuovo utente e le credenziali nel database
	            userService.saveUser(newUser);
	            credentialsService.saveCredentials(newCred);

	            model.addAttribute("user", newUser);
	            model.addAttribute("credentials", newCred);

	            // Aggiungi il ruolo "ESPERTO" alle autorità dell'utente autenticato
	            Collection<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
	            updatedAuthorities.add(new SimpleGrantedAuthority("ESPERTO"));
	            Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
	            SecurityContextHolder.getContext().setAuthentication(newAuth);

	            if (newCred.getRole().equals(Credentials.ADMIN_ROLE)) {
	                return "/admin/indexAdmin.html";
	            } else if (newCred.getRole().equals(Credentials.ESPERTO_ROLE)) {
	                return "/esperto/indexEsperto.html";
	            }
	        }
	    }

	    return "index.html";
	}


	@GetMapping("/success")
	public String defaultAfterLogin(Authentication authentication, Model model) {
	    if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
	    } else if (authentication.getPrincipal() instanceof UserDetails) {
	        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
	        if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	            return "/admin/indexAdmin.html";
	        } else if (credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
	            return "/esperto/indexEsperto.html";
	        }
	    } else if (authentication.getPrincipal() instanceof DefaultOidcUser) {
	        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
	        String email = oidcUser.getAttribute("email");

	        // Verifica se esiste un Credentials con questa email
	        Credentials existingCredentials = credentialsService.getCredentials(email);
	        if (existingCredentials != null) {
	            // Usa le credenziali esistenti
	            model.addAttribute("credentials", existingCredentials);
	            model.addAttribute("user", existingCredentials.getUser());

	            // Aggiungi il ruolo "ESPERTO" se non è già presente
	            if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ESPERTO"))) {
	                Collection<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
	                updatedAuthorities.add(new SimpleGrantedAuthority("ESPERTO"));
	                Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
	                SecurityContextHolder.getContext().setAuthentication(newAuth);
	            }

	            if (existingCredentials.getRole().equals(Credentials.ADMIN_ROLE)) {
	                return "/admin/indexAdmin.html";
	            } else if (existingCredentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
	                return "/esperto/indexEsperto.html";
	            }
	        } else {
	            // Crea nuovo user e credentials
	            Credentials newCred = new Credentials();
	            newCred.setUsername(email);
	            newCred.setPassword(""); // OAuth2 non usa password locali
	            newCred.setRole("ESPERTO");

	            User newUser = new User();
	            String fullName = oidcUser.getAttribute("name");
	            String[] parts = fullName.split(" ");
	            String nome = parts[0];
	            String cognome = parts.length > 1 ? parts[1] : ""; // Evita l'ArrayIndexOutOfBoundsException
	            LocalDate birthday = oidcUser.getAttribute("birthday");

	            newUser.setNascita(birthday);
	            newUser.setNome(nome);
	            newUser.setCognome(cognome);
	            String urlImage = oidcUser.getAttribute("picture");
	            newUser.setUrlImage(urlImage);
	            newUser.setEmail(email);
	            newCred.setUser(newUser);

	            Esperto nuovoEsperto = new Esperto();
	            nuovoEsperto.setNome(newUser.getNome());
	            nuovoEsperto.setCognome(newUser.getCognome());
	            nuovoEsperto.setNascita(newUser.getNascita());
	            nuovoEsperto.setUrlImage(newUser.getUrlImage());
	            nuovoEsperto.setDescrizione(DESCRIZIONE_GOOGLE);
	            espertoService.save(nuovoEsperto);

	            // Salva il nuovo utente e le credenziali nel database
	            userService.saveUser(newUser);
	            credentialsService.saveCredentials(newCred);

	            model.addAttribute("user", newUser);
	            model.addAttribute("credentials", newCred);

	            // Aggiungi il ruolo "ESPERTO" alle autorità dell'utente autenticato
	            Collection<GrantedAuthority> updatedAuthorities = new ArrayList<>(authentication.getAuthorities());
	            updatedAuthorities.add(new SimpleGrantedAuthority("ESPERTO"));
	            Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);
	            SecurityContextHolder.getContext().setAuthentication(newAuth);

	            if (newCred.getRole().equals(Credentials.ADMIN_ROLE)) {
	                return "/admin/indexAdmin.html";
	            } else if (newCred.getRole().equals(Credentials.ESPERTO_ROLE)) {
	                return "/esperto/indexEsperto.html";
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

}
