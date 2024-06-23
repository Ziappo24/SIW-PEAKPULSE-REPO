package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.uniroma3.siw.controller.validator.AttivitaValidator;
import it.uniroma3.siw.model.Attivita;
import it.uniroma3.siw.model.Attrezzatura;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Esperto;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.AttivitaRepository;
import it.uniroma3.siw.repository.AttrezzaturaRepository;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.EspertoRepository;
import it.uniroma3.siw.service.AttivitaService;
import it.uniroma3.siw.service.EspertoService;
import jakarta.validation.Valid;

@Controller
public class AttivitaController {

//	private static String UPLOAD_DIR = "C:\\Users\\EDOARDO\\Desktop\\FOR SISW\\siw-peakpulse-repo\\siw-peakpulse-1\\src\\main\\resources\\static\\images";
	private static String UPLOAD_DIR = "C:\\Users\\utente\\Desktop\\UNIR3\\TERZO ANNO\\II SEMESTRE\\SISW\\siw-peakpulse-repo\\siw-peakpulse-1\\src\\main\\resources\\static\\images";
//	private static String UPLOAD_DIR = "C:\\Users\\UTENTE\\Documents\\workspace-spring-tool-suite-4-4.22.0.RELEASE\\siw-peakpulse-repo\\siw-peakpulse-1\\src\\main\\resources\\static\\images";
	
	@Autowired 
	AttivitaRepository attivitaRepository;
	
	@Autowired 
	AttivitaService attivitaService;
	
	@Autowired
	AttivitaValidator attivitaValidator;
	
	@Autowired
	EspertoRepository espertoRepository;
	
	@Autowired
	EspertoService espertoService;
	
	@Autowired
	CredentialsRepository credentialsRepository;
	
	@Autowired
	AttrezzaturaRepository attrezzaturaRepository;
	
	@GetMapping("/attivita/{id}")
	public String getAttivita(@PathVariable("id") Long id, Model model) {
		Attivita attivita = attivitaService.findById(id);
		model.addAttribute("attivita", attivita);
		return "attivita.html";
	}
	
	@GetMapping(value="/esperto/attivita/{id}")
	public String getAttivitaEsperto(@PathVariable("id") Long id, Model model) {
		Attivita attivita = attivitaService.findById(id);
		model.addAttribute("attivita", attivita);
		return "/esperto/attivita.html";
	}

	@GetMapping("/listaAttivita")
	public String ShowAttivita(Model model) {
		model.addAttribute("listaAttivita", this.attivitaService.findAll());
		return "listaAttivita.html";
	}
	
	@PostMapping("/searchAttivita")
	public String searchRicette(Model model, @RequestParam String nome) {
		model.addAttribute("attivita", this.attivitaRepository.findByNome(nome));
		return "attivita.html";
	}

	@PostMapping("admin/searchAttivita")
	public String searchAttivitaAdmin(Model model, @RequestParam String nome) {
		model.addAttribute("attivita", this.attivitaRepository.findByNome(nome));
		return "/admin/manageAttivita.html";
	}

	@PostMapping("esperto/searchAttivita")
	public String searchAttivitaEsperto(Model model, @RequestParam String nome) {
		model.addAttribute("attivita", this.attivitaRepository.findByNome(nome));
		return "/esperto/manageAttivita.html";
	}

	@GetMapping("/admin/manageAttivita")
	public String ShowAttivitaAdmin(Model model) {
		model.addAttribute("listaAttivita", this.attivitaService.findAll());
		return "/admin/manageAttivita.html";
	}

	@GetMapping("/esperto/manageAttivita")
	public String ShowAttivitaEsperto(Model model) {
		model.addAttribute("listaAttivita", this.attivitaService.findAll());
		return "/esperto/manageAttivita.html";
	}

	@GetMapping(value = "/admin/formNewAttivita")
	public String formNewAttivita(Model model) {
	    Attivita attivita = new Attivita();
	    model.addAttribute("attivita", attivita);
	    return "/admin/formNewAttivita.html";
	}

	@PostMapping("/admin/attivita")
	public String newAttivita(@Valid @ModelAttribute("attivita") Attivita attivita, BindingResult bindingResult,
			@RequestParam("immagine") MultipartFile file, Model model) {
		this.attivitaValidator.validate(attivita, bindingResult);

		if (!bindingResult.hasErrors()) {
			if (!file.isEmpty()) {
				try {
					// Salva il file sul server
					String fileName = StringUtils.cleanPath(file.getOriginalFilename());
					Path path = Paths.get(UPLOAD_DIR + File.separator + fileName);
					Files.write(path, file.getBytes());
					attivita.setUrlImage(fileName);

					// Salva l'attivita
					this.attivitaRepository.save(attivita);

					model.addAttribute("attivita", attivita);
					return "attivita.html";
				} catch (IOException e) {
					e.printStackTrace();
					model.addAttribute("messaggioErrore", "Errore durante il salvataggio dell'immagine");
					return "/admin/formNewAttivita.html";
				}
			} else {
				model.addAttribute("messaggioErrore", "Il file dell'immagine è vuoto");
				return "/admin/formNewAttivita.html";
			}
		} else {
			return "/admin/formNewAttivita.html";
		}
	}

	@GetMapping(value = "/esperto/formNewAttivita/{username}")
	public String formNewAttivitaEsperto(@PathVariable("username") String username, Model model) {
	    Credentials tempUser = credentialsRepository.findByUsername(username);
	    User currentUser = tempUser.getUser();
	    Esperto currentEsperto = this.espertoRepository.findByNomeAndCognome(currentUser.getNome(), currentUser.getCognome());
	    Attivita attivita = new Attivita();
		model.addAttribute("esperto", currentEsperto);
		model.addAttribute("espertoId", currentEsperto.getId());
		model.addAttribute("attivita", attivita);
		model.addAttribute("userDetails", tempUser); // Aggiungi userDetails al modello
		return "esperto/formNewAttivita.html";
	}

	@PostMapping("/esperto/attivita")
	public String newAttivitaEsperto(@Valid @ModelAttribute("attivita") Attivita attivita, BindingResult bindingResult,
			@RequestParam("username") String username, @RequestParam("immagine") MultipartFile file, Model model) {
		Credentials tempUser = credentialsRepository.findByUsername(username);
		User currentUser = tempUser.getUser();
		Esperto currentEsperto = this.espertoRepository.findByNomeAndCognome(currentUser.getNome(), currentUser.getCognome());
		attivita.setEsperto(currentEsperto);

		this.attivitaValidator.validate(attivita, bindingResult);
		if (!bindingResult.hasErrors()) {
			if (!file.isEmpty()) {
				try {
					// Salva il file sul server
					String fileName = StringUtils.cleanPath(file.getOriginalFilename());
					Path path = Paths.get(UPLOAD_DIR + File.separator + fileName);
					Files.write(path, file.getBytes());
					attivita.setUrlImage(fileName);

					// Salva l'attivita
					this.attivitaRepository.save(attivita);

					model.addAttribute("attivita", attivita);
					return "attivita.html";
				} catch (IOException e) {
					e.printStackTrace();
					model.addAttribute("messaggioErrore", "Errore durante il salvataggio dell'immagine");
					return "/esperto/formNewAttivita.html";
				}
			} else {
				model.addAttribute("messaggioErrore", "Il file dell'immagine è vuoto");
				return "/esperto/formNewAttivita.html";
			}
		} else {
			return "/esperto/formNewAttivita.html";
		}
	}

	@GetMapping(value = "/admin/addEsperto/{idAttivita}")
	public String addEsperto(@PathVariable("idAttivita") Long attivitaId, Model model) {
		model.addAttribute("esperti", espertoService.findAll());
		model.addAttribute("attivita", attivitaRepository.findById(attivitaId).get());
		return "/admin/addEsperto.html";
	}

	@GetMapping(value = "/admin/formUpdateAttivita/{id}")
	public String formUpdateAttivita(@PathVariable("id") Long id, Model model) {
		model.addAttribute("attivita", attivitaRepository.findById(id).get());
		return "admin/formUpdateAttivita.html";
	}

	@GetMapping(value = "/esperto/formUpdateAttivita/{id}/{username}")
	public String formUpdateAttivitaEsperto(@PathVariable("id") Long id, @PathVariable("username") String username,
			Model model, RedirectAttributes redirectAttributes) {
		// Recupera l'utente dal repository
		Credentials tempUser = credentialsRepository.findByUsername(username);
		User currentUser = tempUser.getUser();

		// Recupera L'attivita dal repository
		Attivita attivita = attivitaRepository.findById(id).orElse(null);

		// Verifica se esperto dell'attività è il esperto corrente
		if (attivita == null || attivita.getEsperto() == null || !attivita.getEsperto().getNome().equals(currentUser.getNome())
				|| !attivita.getEsperto().getCognome().equals(currentUser.getCognome())) {
			// Gestisci il caso di accesso non autorizzato
			redirectAttributes.addFlashAttribute("messaggioErrore",
					"Non puoi modificare questa attivita perché non ti appartiene!");
			return "redirect:/esperto/manageAttivita";
		}

		// Aggiungi l'attività al modello e restituisci la vista
		model.addAttribute("attivita", attivita);
		return "esperto/formUpdateAttivita.html";
	}

	@GetMapping(value = "/admin/setEspertoToAttivita/{espertoId}/{attivitaId}")
	public String setEspertoToAttivita(@PathVariable("espertoId") Long espertoId, @PathVariable("attivitaId") Long attivitaId,
			Model model) {

		Esperto esperto = this.espertoService.findById(espertoId);
		Attivita attivita = this.attivitaRepository.findById(attivitaId).get();
		attivita.setEsperto(esperto);
		this.attivitaRepository.save(attivita);

		model.addAttribute("attivita", attivita);
		return "admin/formUpdateAttivita.html";
	}
	
	private List<Attrezzatura> attrezzaturaToAdd(Long attivitaId) {
		List<Attrezzatura> attrezzaturaToAdd = new ArrayList<>();

		for (Attrezzatura a : attrezzaturaRepository.findAttrezzatureNotInAttivita(attivitaId)) {
			attrezzaturaToAdd.add(a);
		}
		return attrezzaturaToAdd;
	}
	
	/* per aggiungere o togliere attrezzi a mo di lista */
	@GetMapping("/admin/updateAttrezzatura/{id}")
	public String updateAttrezzatura(@PathVariable("id") Long id, Model model) {

		List<Attrezzatura> attrezzaturaToAdd = this.attrezzaturaToAdd(id);
		model.addAttribute("attrezzaturaToAdd", attrezzaturaToAdd);
		model.addAttribute("attivita", this.attivitaRepository.findById(id).get());

		return "admin/addAttrezzatura.html";
	}

	@GetMapping("/esperto/updateAttrezzatura/{id}")
	public String updateAttrezzaturaEsperto(@PathVariable("id") Long id, Model model) {

		List<Attrezzatura> attrezzaturaToAdd = this.attrezzaturaToAdd(id);
		model.addAttribute("attrezzaturaToAdd", attrezzaturaToAdd);
		model.addAttribute("attivita", this.attivitaRepository.findById(id).get());

		return "esperto/addAttrezzatura.html";
	}

	@GetMapping(value = "/admin/addAttrezzaturaToAttivita/{attrezzaturaId}/{attivitaId}")
	public String addAttrezzaturaToAttivita(@PathVariable("attrezzaturaId") Long attrezzaturaId,
			@PathVariable("attivitaId") Long attivitaId, Model model) {
		Attivita attivita = this.attivitaRepository.findById(attivitaId).get();
		Attrezzatura attrezzatura = this.attrezzaturaRepository.findById(attrezzaturaId).get();
		List<Attrezzatura> attrezzature = attivita.getAttrezzatureUtilizzate();
		attrezzature.add(attrezzatura);
		this.attivitaRepository.save(attivita);

		List<Attrezzatura> attrezzaturaToAdd = attrezzaturaToAdd(attivitaId);

		model.addAttribute("attivita", attivita);
		model.addAttribute("attrezzaturaToAdd", attrezzaturaToAdd);

		return "admin/addAttrezzatura.html";
	}

	@GetMapping(value = "/esperto/addAttrezzaturaToAttivita/{attrezzaturaId}/{attivitaId}")
	public String addAttrezzaturaToAttivitaEsperto(@PathVariable("attrezzaturaId") Long attrezzaturaId,
			@PathVariable("attivitaId") Long attivitaId, Model model) {
		Attivita attivita = this.attivitaRepository.findById(attivitaId).get();
		Attrezzatura attrezzatura = this.attrezzaturaRepository.findById(attrezzaturaId).get();
		List<Attrezzatura> attrezzature = attivita.getAttrezzatureUtilizzate();
		attrezzature.add(attrezzatura);
		this.attivitaRepository.save(attivita);

		List<Attrezzatura> attrezzaturaToAdd = attrezzaturaToAdd(attivitaId);

		model.addAttribute("attivita", attivita);
		model.addAttribute("attrezzaturaToAdd", attrezzaturaToAdd);

		return "esperto/addAttrezzatura.html";
	}

	@GetMapping(value = "/admin/removeAttrezzaturaFromAttivita/{attrezzaturaId}/{attivitaId}")
	public String removeAttrezzaturaFromAttivita(@PathVariable("attrezzaturaId") Long attrezzaturaId,
			@PathVariable("attivitaId") Long attivitaId, Model model) {
		Attivita attivita = this.attivitaRepository.findById(attivitaId).get();
		Attrezzatura attrezzatura = this.attrezzaturaRepository.findById(attrezzaturaId).get();
		List<Attrezzatura> attrezzatureUtilizzate = attivita.getAttrezzatureUtilizzate();
		attrezzatureUtilizzate.remove(attrezzatura);
		this.attivitaRepository.save(attivita);

		List<Attrezzatura> attrezzaturaToAdd = attrezzaturaToAdd(attivitaId);

		model.addAttribute("attivita", attivita);
		model.addAttribute("attrezzaturaToAdd", attrezzaturaToAdd);

		return "admin/addAttrezzatura.html";
	}

	@GetMapping(value = "/esperto/removeAttrezzaturaFromAttivita/{attrezzaturaId}/{attivitaId}")
	public String removeAttrezzaturaFromAttivitaEsperto(@PathVariable("attrezzaturaId") Long attrezzaturaId,
			@PathVariable("attivitaId") Long attivitaId, Model model) {
		Attivita attivita = this.attivitaRepository.findById(attivitaId).get();
		Attrezzatura attrezzatura = this.attrezzaturaRepository.findById(attrezzaturaId).get();
		List<Attrezzatura> attrezzatureUtilizzate = attivita.getAttrezzatureUtilizzate();
		attrezzatureUtilizzate.remove(attrezzatura);
		this.attivitaRepository.save(attivita);

		List<Attrezzatura> attrezzaturaToAdd = attrezzaturaToAdd(attivitaId);

		model.addAttribute("attivita", attivita);
		model.addAttribute("attrezzaturaToAdd", attrezzaturaToAdd);

		return "esperto/addAttrezzatura.html";
	}

	@GetMapping(value = "/admin/deleteAttivita/{attivitaId}")
	public String deleteAttivitaAdmin(@PathVariable("attivitaId") Long attivitaId, Model model) {
		Attivita attivita = attivitaService.findById(attivitaId);
		attivita.setAttrezzatureUtilizzate(null);
		attivitaService.deleteById(attivitaId);
		return "redirect:/admin/manageAttivita";
	}

	@GetMapping(value = "/esperto/deleteAttivita/{attivitaId}")
	public String deleteAttivitaAdminEsperto(@PathVariable("attivitaId") Long attivitaId, Model model) {
		Attivita attivita = attivitaService.findById(attivitaId);
		attivita.setAttrezzatureUtilizzate(null);
		attivitaService.deleteById(attivita.getId());
		return "redirect:/esperto/manageAttivita";
	}
}
