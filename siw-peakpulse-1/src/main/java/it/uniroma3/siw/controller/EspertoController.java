package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Esperto;
import it.uniroma3.siw.repository.EspertoRepository;
import it.uniroma3.siw.service.EspertoService;

@Controller
public class EspertoController {
	
	private static String UPLOAD_DIR = "C:\\Users\\EDOARDO\\Desktop\\FOR SISW\\siw-peakpulse-repo\\siw-food-2\\src\\main\\resources\\static\\images";
	
	@Autowired
	EspertoRepository espertoRepository;

	@Autowired
	EspertoService espertoService;
	
	@GetMapping("/esperto/{id}")
	public String getEsperto(@PathVariable("id") Long id, Model model) {
		Esperto esperto = espertoService.findById(id);
		model.addAttribute("esperto", esperto);
		return "esperto.html";
	}

	@GetMapping("/esperti")
	public String ShowEsperto(Model model) {
		model.addAttribute("esperti", this.espertoService.findAll());
		return "esperti.html";
	}
}
