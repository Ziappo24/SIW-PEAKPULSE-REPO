package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Recensione;

public interface RecensioneRepository extends CrudRepository<Recensione, Long>{
	
	@Query("SELECT r FROM Recensione r WHERE r.numeroStelle = :numeroStelle")
	public List<Recensione> findByNumeroStelle(@Param("numeroStelle") Integer numeroStelle);
	
}
