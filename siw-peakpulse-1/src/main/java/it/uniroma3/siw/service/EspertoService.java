package it.uniroma3.siw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Esperto;
import it.uniroma3.siw.repository.EspertoRepository;

@Service
public class EspertoService {
	
	@Autowired EspertoRepository espertoRepository;
	public Esperto findById(Long id) {
		return espertoRepository.findById(id).get();
	}
	
	public Iterable<Esperto> findAll() {
		return espertoRepository.findAll();
	}
	
	public Esperto save(Esperto esperto) {
		return espertoRepository.save(esperto);
	}
	
	
	public Object findByNome(String nome) {
		return espertoRepository.findByNome(nome);
	}
	
	public Object findByNomeAndCognome(String nome, String cognome) {
		return espertoRepository.findByNome(nome);
	}
	
	public void deleteById(Long id) {
		espertoRepository.deleteById(id);
    }
}
