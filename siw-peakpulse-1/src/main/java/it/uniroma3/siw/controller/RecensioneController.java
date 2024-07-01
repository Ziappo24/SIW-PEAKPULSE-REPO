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
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.RecensioneService;

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

	@Autowired
	UserRepository userRepository;

	@Autowired
	RecensioneService recensioneService;

	@GetMapping(value = "/recensione/{idRecensione}")
	public String showRecensione(@PathVariable Long idRecensione, Model model) {
		Recensione recensione = recensioneRepository.findById(idRecensione).get();
		Esperto esperto = espertoRepository.findByNomeAndCognome(recensione.getAutore().getNome(),
				recensione.getAutore().getCognome());
		model.addAttribute("recensione", recensione);
		model.addAttribute("esperto", esperto);
		return "recensione.html";
	}
	
	@GetMapping("/recensioni")
	public String ShowRecensioni(Model model) {
		model.addAttribute("recensioni", this.recensioneService.findAll());
		return "recensioni.html";
	}

	@GetMapping(value = "/esperto/formNewRecensione/{idAttivita}/{username}")
	public String formNewRecensione(@PathVariable Long idAttivita, @PathVariable String username, Model model) {
		// Trova le credenziali dell'utente
		Credentials tempUser = credentialsRepository.findByUsername(username);
		User currentUser;

		if (tempUser == null) {
			String[] parts = username.split(" ");
			String nome = parts[0];
			String cognome = parts.length > 1 ? parts[1] : "";
			currentUser = userRepository.findByNomeAndCognome(nome, cognome);
		} else {
			currentUser = tempUser.getUser();
		}

		// Trova l'utente associato alle credenziali
		Esperto currentEsperto = espertoRepository.findByNomeAndCognome(currentUser.getNome(),
				currentUser.getCognome());
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
		model.addAttribute("user", currentUser);
		model.addAttribute("attivita", attivita);

		return "esperto/formNewRecensione";
	}

	@PostMapping(value = "/esperto/recensione")
	public String newRecensione(@ModelAttribute("recensione") Recensione recensione,
			@RequestParam("username") String username, @RequestParam("attivita") Long idAttivita,
			@RequestParam("voto") Integer voto, Model model) {

		Credentials tempUser = credentialsRepository.findByUsername(username);
		User currentUser;

		if (tempUser == null) {
			String[] parts = username.split(" ");
			String nome = parts[0];
			String cognome = parts.length > 1 ? parts[1] : "";
			currentUser = userRepository.findByNomeAndCognome(nome, cognome);
		} else {
			currentUser = tempUser.getUser();
		}
		Esperto currentEsperto = this.espertoRepository.findByNomeAndCognome(currentUser.getNome(),
				currentUser.getCognome());
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
		model.addAttribute("recensione", recensione);
		model.addAttribute("esperto", currentEsperto);
		model.addAttribute("user", currentUser);
		model.addAttribute("attivita", attivita);

		return "/esperto/attivita.html";
	}

	/* fare delete recensione */
	@GetMapping(value = "/esperto/deleteRecensione/{idRecensione}")
	public String deleteRecensioneEsperto(@PathVariable("idRecensione") Long idRecensione, Model model) {
		Recensione recensione = recensioneService.findById(idRecensione);
		recensioneService.deleteById(recensione.getId());
		return "redirect:/esperto/manageAttivita";
	}
	
	@PostMapping("/searchRecensioni")
	public String searchRecensione(Model model, @RequestParam Integer numeroStelle) {
		model.addAttribute("recensioni", this.recensioneRepository.findByNumeroStelle(numeroStelle));
		return "recensioni.html";
	}

}
