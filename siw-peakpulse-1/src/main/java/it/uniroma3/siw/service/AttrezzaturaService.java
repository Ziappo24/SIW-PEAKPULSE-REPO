package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Attivita;
import it.uniroma3.siw.model.Attrezzatura;
import it.uniroma3.siw.repository.AttivitaRepository;
import it.uniroma3.siw.repository.AttrezzaturaRepository;
import jakarta.transaction.Transactional;

@Service
public class AttrezzaturaService {
	
	@Autowired
	AttrezzaturaRepository attrezzaturaRepository;
	@Autowired
	AttivitaRepository attivitaRepository;


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
	
	@Transactional
	public void deleteAttrezzatura(Long attrezzaturaId) {
		Optional<Attrezzatura> optionalAttrezzatura = attrezzaturaRepository.findById(attrezzaturaId);
		
		if(optionalAttrezzatura.isPresent()) {
			Attrezzatura attrezzatura = optionalAttrezzatura.get();
			//rimuovi l'associazione con le attivita
			for(Attivita attivita : attivitaRepository.findAllByAttrezzatureUtilizzate(attrezzatura)) {
				attivita.getAttrezzatureUtilizzate().remove(attrezzatura);
				attivitaRepository.save(attivita);
			}
			attrezzaturaRepository.delete(attrezzatura);
		}
		
	}
}
