package it.uniroma3.siw.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Esperto;

public interface EspertoRepository extends CrudRepository<Esperto, Long>{
	
	@Query("SELECT e FROM Esperto e WHERE LOWER(e.nome) = LOWER(?1)")
	public List<Esperto> findByNome(String nome);

	public Optional<Esperto> findById(Long Id);

	public Esperto findByNomeAndCognome(String nome, String cognome);

	public boolean existsByNomeAndCognome(String nome, String cognome);

}
