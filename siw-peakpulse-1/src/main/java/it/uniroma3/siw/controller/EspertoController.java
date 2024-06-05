package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	
	@GetMapping(value = "/admin/formNewEsperto")
	public String formNewEsperto(Model model) {
		model.addAttribute("esperto", new Esperto());
		return "admin/formNewEsperto.html";
	}

	@GetMapping("/admin/manageEsperti")
	public String ShowEspertoAdmin(Model model) {
		model.addAttribute("esperti", this.espertoService.findAll());
		return "/admin/manageEsperti.html";
	}

	@PostMapping("/searchEsperti")
	public String searchEsperti(Model model, @RequestParam String nome) {
		model.addAttribute("esperti", this.espertoRepository.findByNome(nome));
		return "esperti.html";
	}

	@PostMapping("admin/searchEsperti")
	public String searchEspertiAdmin(Model model, @RequestParam String nome) {
		model.addAttribute("esperti", this.espertoRepository.findByNome(nome));
		return "/admin/manageEsperti.html";
	}

	@PostMapping("/admin/esperti")
	public String newEsperto(@ModelAttribute("esperti") Esperto esperto, @RequestParam("immagine") MultipartFile file, Model model) {
	    if (!espertoRepository.existsByNomeAndCognome(esperto.getNome(), esperto.getCognome())) {
	        if (!file.isEmpty()) {
	            try {
	                // Salva il file sul server
	                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	                Path path = Paths.get(UPLOAD_DIR + File.separator + fileName);
	                Files.write(path, file.getBytes());
	                esperto.setUrlImage(fileName);
	                
	                // Salva l'esperto
	                this.espertoService.save(esperto);
	                
	                model.addAttribute("esperto", esperto);
	                return "esperto.html";
	            } catch (IOException e) {
	                e.printStackTrace();
	                model.addAttribute("messaggioErrore", "Errore durante il salvataggio dell'immagine");
	                return "formNewEsperto";
	            }
	        } else {
	            model.addAttribute("messaggioErrore", "Il file dell'immagine è vuoto");
	            return "formNewEsperto";
	        }
	    } else {
	        model.addAttribute("messaggioErrore", "Questo Esperto esiste già");
	        return "formNewEsperto";
	    }
	}


	@GetMapping(value = "/admin/deleteEsperto/{espertoId}")
	public String deleteCuocoAdmin(@PathVariable("espertoId") Long espertoId, Model model) {
		espertoService.deleteById(espertoId);
		return "redirect:/admin/manageEsperti";
	}
}
