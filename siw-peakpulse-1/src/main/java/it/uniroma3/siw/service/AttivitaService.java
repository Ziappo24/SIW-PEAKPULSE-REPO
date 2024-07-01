package it.uniroma3.siw.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Attivita;
import it.uniroma3.siw.repository.AttivitaRepository;

@Service
public class AttivitaService {
	
	@Autowired AttivitaRepository attivitaRepository;
	
	public Attivita findById(Long id) {
		return attivitaRepository.findById(id).get();
	}
	
	public Iterable<Attivita> findAll() {
		return attivitaRepository.findAll();
	}
	
	public Attivita save(Attivita attivita) {
		return attivitaRepository.save(attivita);
	}
	
	public void deleteById(Long id) {
		attivitaRepository.deleteById(id);
    }
}
