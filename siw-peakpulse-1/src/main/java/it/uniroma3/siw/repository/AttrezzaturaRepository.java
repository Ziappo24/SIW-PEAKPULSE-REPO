package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Attrezzatura;

public interface AttrezzaturaRepository extends CrudRepository<Attrezzatura, Long>{
	
	@Query("SELECT ar FROM Attrezzatura ar WHERE LOWER(ar.nome) = LOWER(?1)")
	public List<Attrezzatura> findByNome(String nome);
	
	
	public boolean existsByNome(String nome);
	
	@Query(value="select * "
	        + "from attrezzatura a "
	        + "where a.id not in "
	        + "(select aa.attrezzature_utilizzate_id "
	        + "from attivita_attrezzature_utilizzate aa "
	        + "where aa.attivita_id = :attivitaId)", nativeQuery=true)
	public Iterable<Attrezzatura> findAttrezzatureNotInAttivita(@Param("attivitaId") Long attivitaId);
}
