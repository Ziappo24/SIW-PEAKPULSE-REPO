package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
	
	private static String UPLOAD_DIR = "C:\\Users\\EDOARDO\\Desktop\\FOR SISW\\siw-peakpulse-repo\\siw-peakpulse-1\\src\\main\\resources\\static\\images";
	
	@Autowired
	private CredentialsService credentialsService;

    @Autowired
	private UserService userService;
    
    
    @Autowired
    private EspertoService espertoService;
	
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formRegisterUser";
	}
	
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "formLogin.html";
	}
	
	@GetMapping (value = "/login/error")
	public String showLoginErrorForm(Model model) {
		String messaggioErrore = new String("Username o password incorretti");
		model.addAttribute("messaggioErrore", messaggioErrore);
		return "formLogin.html";
	}

	@GetMapping(value = "/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "admin/indexAdmin.html";
			}
			if (credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
				return "esperto/indexEsperto.html";
			}
		}
        return "index.html";
	}
		
    @GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
        
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
            return "admin/indexAdmin.html";
        }
    	if (credentials.getRole().equals(Credentials.ESPERTO_ROLE)) {
            return "esperto/indexEsperto.html";
        }
        return "index.html";
    }
	
	@PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid
                 @ModelAttribute("credentials") Credentials credentials,
                 BindingResult credentialsBindingResult, @RequestParam("immagine") MultipartFile file,
                 Model model) {

		// se user e credential hanno entrambi contenuti validi, memorizza User e the Credentials nel DB
		 if (!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
	            if (!file.isEmpty()) {
	                try {
	                    // Salva il file sul server
	                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	                    Path path = Paths.get(UPLOAD_DIR + File.separator + fileName);
	                    Files.write(path, file.getBytes());
	                    user.setUrlImage(fileName);  // Assumi che l'entità User abbia un campo urlImage
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
