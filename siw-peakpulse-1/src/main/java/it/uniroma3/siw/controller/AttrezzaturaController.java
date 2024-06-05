package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Attrezzatura;
import it.uniroma3.siw.repository.AttrezzaturaRepository;
import it.uniroma3.siw.service.AttrezzaturaService;

@Controller
public class AttrezzaturaController {
private static String UPLOAD_DIR = "C:\\Users\\EDOARDO\\Desktop\\FOR SISW\\siw-peakpulse-repo\\siw-food-2\\src\\main\\resources\\static\\images";
	
	@Autowired 
	AttrezzaturaRepository attrezzaturaRepository;
	
	@Autowired 
	AttrezzaturaService attrezzaturaService;
	
	@GetMapping("/attrezzatura/{id}")
	public String getAttrezzatura(@PathVariable("id") Long id, Model model) {
		Attrezzatura attrezzatura = attrezzaturaService.findById(id);
		model.addAttribute("attrezzatura", attrezzatura);
		return "attrezzatura.html";
	}

	@GetMapping("/attrezzature")
	public String ShowAttrezzaturac(Model model) {
		model.addAttribute("attrezzature", this.attrezzaturaService.findAll());
		return "attrezzature.html";
	}
}
