package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Attivita;
import it.uniroma3.siw.model.Esperto;
import it.uniroma3.siw.repository.AttivitaRepository;
import it.uniroma3.siw.service.AttivitaService;

@Controller
public class AttivitaController {

	private static String UPLOAD_DIR = "C:\\Users\\EDOARDO\\Desktop\\FOR SISW\\siw-peakpulse-repo\\siw-food-2\\src\\main\\resources\\static\\images";
	
	@Autowired 
	AttivitaRepository attivitaRepository;
	
	@Autowired 
	AttivitaService attivitaService;
	
	@GetMapping("/attivita/{id}")
	public String getAttivita(@PathVariable("id") Long id, Model model) {
		Attivita attivita = attivitaService.findById(id);
		model.addAttribute("attivita", attivita);
		return "attivita.html";
	}

	@GetMapping("/listaAttivita")
	public String ShowAttivita(Model model) {
		model.addAttribute("listaAttivita", this.attivitaService.findAll());
		return "listaAttivita.html";
	}
}
