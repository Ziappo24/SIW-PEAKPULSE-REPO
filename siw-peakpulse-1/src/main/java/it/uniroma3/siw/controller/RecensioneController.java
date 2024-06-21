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
	public String formNewRecensione(@PathVariable("idAttivita") Long idAttivita, @PathVariable("username") String username,  Model model) {
		Credentials tempUser = credentialsRepository.findByUsername(username);
	    User currentUser = tempUser.getUser();
	    Esperto currentEsperto = this.espertoRepository.findByNomeAndCognome(currentUser.getNome(), currentUser.getCognome());
	    Optional<Attivita> tempAttivita = this.attivitaRepository.findById(idAttivita);
	    Attivita attivita = tempAttivita.get();
	    Recensione recensione = new Recensione();
	    model.addAttribute("recensione", recensione);
	    model.addAttribute("esperto", currentEsperto);
		model.addAttribute("espertoId", currentEsperto.getId());
		model.addAttribute("userDetails", tempUser);
		model.addAttribute("attivita", attivita);
		model.addAttribute("attivitaId", attivita.getId());
	    return "esperto/formNewRecensione.html";
	}
	
	@PostMapping("/esperto/recensione")
	public String newRecensione(@ModelAttribute("recensione") Recensione recensione, @RequestParam("username") String username,@RequestParam("attivita") Long idAttivita, Model model) {
		Credentials tempUser = credentialsRepository.findByUsername(username);
		User currentUser = tempUser.getUser();
		Esperto currentEsperto = this.espertoRepository.findByNomeAndCognome(currentUser.getNome(), currentUser.getCognome());
		recensione.setAutore(currentEsperto);
		Optional<Attivita> tempAttivita = this.attivitaRepository.findById(idAttivita);
	    Attivita attivita = tempAttivita.get();
	    recensione.setAttivita(attivita);
		this.recensioneRepository.save(recensione);
		List<Recensione> recensioni = attivita.getRecensioni();
		recensioni.add(recensione);
		attivita.setRecensioni(recensioni);
		return "attivita.html";
	}

}
