package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import it.uniroma3.siw.model.Recensione;
import it.uniroma3.siw.repository.RecensioneRepository;

@Service
public class RecensioneService {

	@Autowired
	RecensioneRepository recensioneRepository;

	public Recensione findById(Long id) {
		return recensioneRepository.findById(id).get();
	}

	public Iterable<Recensione> findAll() {
		return recensioneRepository.findAll();
	}

	public Recensione save(Recensione recensione) {
		return recensioneRepository.save(recensione);
	}

	public void deleteById(Long id) {
		recensioneRepository.deleteById(id);
	}
	
	public Object findByNumeroStelle(Integer numeroStelle) {
		return recensioneRepository.findByNumeroStelle(numeroStelle);
	}
}
