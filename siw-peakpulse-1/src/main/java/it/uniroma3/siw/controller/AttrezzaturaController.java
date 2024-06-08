package it.uniroma3.siw.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Attrezzatura;
import it.uniroma3.siw.repository.AttrezzaturaRepository;
import it.uniroma3.siw.service.AttrezzaturaService;

@Controller
public class AttrezzaturaController {
private static String UPLOAD_DIR = "C:\\Users\\EDOARDO\\Desktop\\FOR SISW\\siw-peakpulse-repo\\siw-peakpulse-1\\src\\main\\resources\\static\\images";
	
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
	public String ShowAttrezzatura(Model model) {
		model.addAttribute("attrezzature", this.attrezzaturaService.findAll());
		return "attrezzature.html";
	}
	
	@PostMapping("/searchAttrezzature")
	public String searchAttrezzature(Model model, @RequestParam String nome) {
		model.addAttribute("attrezzature", this.attrezzaturaRepository.findByNome(nome));
		return "attrezzatture.html";
	}
	

	@PostMapping("admin/searchAttrezzature")
	public String searchAttrezzatureAdmin(Model model, @RequestParam String nome) {
		model.addAttribute("attrezzature", this.attrezzaturaRepository.findByNome(nome));
		return "/admin/manageAttrezzature.html";
	}

	
	@GetMapping("/admin/manageAttrezzature")
	public String ShowAttrezzatureAdmin(Model model) {
		model.addAttribute("attrezzature", this.attrezzaturaService.findAll());
		return "/admin/manageAttrezzature.html";
	}
	
	@GetMapping(value = "/admin/formNewAttrezzatura")
	public String formNewAttrezzatura(Model model) {
		model.addAttribute("attrezzatura", new Attrezzatura());
		return "/admin/formNewAttrezzatura.html";
	}
	
	@PostMapping("/admin/attrezzatura")
	public String newIngrediente(@ModelAttribute("attrezzatura") Attrezzatura attrezzatura, 
	                             @RequestParam("immagine") MultipartFile file, Model model) {
	    if (!attrezzaturaRepository.existsByNome(attrezzatura.getNome())) {
	        if (!file.isEmpty()) {
	            try {
	                // Salva il file sul server
	                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	                Path path = Paths.get(UPLOAD_DIR + File.separator + fileName);
	                Files.write(path, file.getBytes());
	                attrezzatura.setUrlImage(fileName);

	                // Salva l'ingrediente
	                this.attrezzaturaService.save(attrezzatura);
	                model.addAttribute("attrezzatura", attrezzatura);
	                return "attrezzatura.html";
	            } catch (IOException e) {
	                e.printStackTrace();
	                model.addAttribute("messaggioErrore", "Errore durante il salvataggio dell'immagine");
	                return "admin/formNewAttrezzatura";
	            }
	        } else {
	            model.addAttribute("messaggioErrore", "Il file dell'immagine è vuoto");
	            return "admin/formNewAttrezzatura";
	        }
	    } else {
	        model.addAttribute("messaggioErrore", "Questa attrezzatura esiste già");
	        return "admin/formNewAttrezzatura";
	    }
	}

	
	@GetMapping(value="/esperto/formNewAttrezzatura")
	public String formNewAttrezzaturaEsperto(Model model) {
	    model.addAttribute("attrezzatura", new Attrezzatura());
		return "esperto/formNewAttrezzatura.html";
	}
	
	@PostMapping("/esperto/attrezzatura")
	public String newIngredienteEsperto(@ModelAttribute("attrezzatura") Attrezzatura attrezzatura, 
	                             @RequestParam("immagine") MultipartFile file, Model model) {
	    if (!attrezzaturaRepository.existsByNome(attrezzatura.getNome())) {
	        if (!file.isEmpty()) {
	            try {
	                // Salva il file sul server
	                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	                Path path = Paths.get(UPLOAD_DIR + File.separator + fileName);
	                Files.write(path, file.getBytes());
	                attrezzatura.setUrlImage(fileName);

	                // Salva l'ingrediente
	                this.attrezzaturaService.save(attrezzatura);
	                model.addAttribute("attrezzatura", attrezzatura);
	                return "attrezzatura.html";
	            } catch (IOException e) {
	                e.printStackTrace();
	                model.addAttribute("messaggioErrore", "Errore durante il salvataggio dell'immagine");
	                return "esperto/formNewAttrezzatura";
	            }
	        } else {
	            model.addAttribute("messaggioErrore", "Il file dell'immagine è vuoto");
	            return "esperto/formNewAttrezzatura";
	        }
	    } else {
	        model.addAttribute("messaggioErrore", "Questa attrezzatura esiste già");
	        return "esperto/formNewAttrezzatura";
	    }
	}

	
	@GetMapping(value = "/admin/deleteAttrezzatura/{attrezzaturaId}")
	public String deleteIngredienteAdmin(@PathVariable("attrezzaturaId") Long attrezzaturaId, Model model) {
		attrezzaturaService.deleteById(attrezzaturaId);
        return "redirect:/admin/manageAttrezzatura";
	}
}
