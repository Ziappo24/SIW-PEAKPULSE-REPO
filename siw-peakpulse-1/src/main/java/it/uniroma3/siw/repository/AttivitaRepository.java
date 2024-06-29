package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Attivita;
import it.uniroma3.siw.model.Attrezzatura;
import it.uniroma3.siw.model.Esperto;


public interface AttivitaRepository extends CrudRepository<Attivita, Long> {
	
	public List<Attivita> findByNome(String nome);

	public Optional<Attivita> findById(Long Id);

	public boolean existsByNome(String nome);

	public boolean existsByNomeAndEsperto(String nome, Esperto esperto);

	public Attivita[] findAllByAttrezzatureUtilizzate(Attrezzatura attrezzatura);
}
