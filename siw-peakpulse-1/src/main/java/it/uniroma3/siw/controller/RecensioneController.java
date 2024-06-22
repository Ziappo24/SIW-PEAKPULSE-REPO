package it.uniroma3.siw.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import it.uniroma3.siw.model.Attivita;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Esperto;
import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.AttivitaRepository;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.EspertoRepository;
import it.uniroma3.siw.repository.RecensioneRepository;


@Controller
public class RecensioneController {
	
	@Autowired
	EspertoRepository espertoRepository;
	
	@Autowired
	CredentialsRepository credentialsRepository;
	
	@Autowired 
	AttivitaRepository attivitaRepository;
	
	@Autowired 
	RecensioneRepository recensioneRepository;
	
	@GetMapping(value = "/esperto/formNewRecensione/{idAttivita}/{username}")
	public String formNewRecensione(@PathVariable Long idAttivita, @PathVariable String username, Model model) {
	    // Trova le credenziali dell'utente
	    Credentials tempUser = credentialsRepository.findByUsername(username);
	    if (tempUser == null) {
	        model.addAttribute("messaggioErrore", "Utente non trovato");
	        return "attivita.html";
	    }

	    // Trova l'utente associato alle credenziali
	    User currentUser = tempUser.getUser();
	    Esperto currentEsperto = espertoRepository.findByNomeAndCognome(currentUser.getNome(), currentUser.getCognome());
	    if (currentEsperto == null) {
	        model.addAttribute("messaggioErrore", "Esperto non trovato");
	        return "attivita.html";
	    }

	    // Trova l'attività per ID
	    Optional<Attivita> tempAttivita = attivitaRepository.findById(idAttivita);
	    if (tempAttivita.isEmpty()) {
	        model.addAttribute("messaggioErrore", "Attività non trovata");
	        return "attivita.html";
	    }

	    Attivita attivita = tempAttivita.get();
	    Recensione recensione = new Recensione();

	    // Aggiungi gli attributi al modello
	    model.addAttribute("recensione", recensione);
	    model.addAttribute("esperto", currentEsperto);
	    model.addAttribute("userDetails", tempUser);
	    model.addAttribute("attivita", attivita);

	    return "esperto/formNewRecensione";
	}


	
	@PostMapping(value = "/esperto/recensione")
	public String newRecensione(
	        @ModelAttribute("recensione") Recensione recensione,
	        @RequestParam("username") String username,
	        @RequestParam("attivita") Long idAttivita,
	        @RequestParam("voto") Integer voto,
	        Model model) {
	    
	    Credentials tempUser = credentialsRepository.findByUsername(username);
	    if (tempUser == null) {
	        // handle the case where the user is not found
	        model.addAttribute("messaggioErrore", "Utente non trovato");
	        return "attivita.html";
	    }
	    
	    User currentUser = tempUser.getUser();
	    Esperto currentEsperto = this.espertoRepository.findByNomeAndCognome(currentUser.getNome(), currentUser.getCognome());
	    if (currentEsperto == null) {
	        // handle the case where the expert is not found
	        model.addAttribute("messaggioErrore", "Esperto non trovato");
	        return "attivita.html";
	    }
	    
	    recensione.setAutore(currentEsperto);
	    
	    Optional<Attivita> tempAttivita = this.attivitaRepository.findById(idAttivita);
	    if (!tempAttivita.isPresent()) {
	        // handle the case where the activity is not found
	        model.addAttribute("messaggioErrore", "Attività non trovata");
	        return "attivita.html";
	    }
	    
	    Attivita attivita = tempAttivita.get();
	    recensione.setAttivita(attivita);
	    recensione.setNumeroStelle(voto);
	    
	    this.recensioneRepository.save(recensione);
	    
	    List<Recensione> recensioni = attivita.getRecensioni();
	    recensioni.add(recensione);
	    attivita.setRecensioni(recensioni);
	    
	    return "/esperto/attivita.html";
	}

}
