package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Attrezzatura;
import it.uniroma3.siw.repository.AttrezzaturaRepository;

@Service
public class AttrezzaturaService {
	@Autowired
	AttrezzaturaRepository attrezzaturaRepository;


	public Attrezzatura findById(Long id) {
		return attrezzaturaRepository.findById(id).get();
	}

	public Iterable<Attrezzatura> findAll() {
		return attrezzaturaRepository.findAll();
	}

	public Attrezzatura save(Attrezzatura attrezzatura) {
		return attrezzaturaRepository.save(attrezzatura);
	}

	public Iterable<Attrezzatura> findByName(String nome) {
		return attrezzaturaRepository.findByNome(nome);
	}

	public void deleteById(Long id) {
		attrezzaturaRepository.deleteById(id);
	}
}
