package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Esperto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	public String nome;
	
	public String cognome;
	
	public String urlImage;
	
	
	@Transient
	private MultipartFile immagine;
	
	public LocalDate nascita;
	
	@Column(length = 5000)
	public String descrizione;
	
	/* esegue un joincolumn standard con la tabella ricetta, che avrà una colonna cuoco_id*/
//	@OneToMany(mappedBy = "esperto", fetch = FetchType.EAGER)
//	private List<Attivita> attivita;
	
	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}


//	public List<Ricetta> getRicette() {
//		return ricette;
//	}
//
//	public void setRicette(List<Ricetta> ricette) {
//		this.ricette = ricette;
//	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public LocalDate getNascita() {
		return nascita;
	}

	public void setNascita(LocalDate nascita) {
		this.nascita = nascita;
	}
	
	@Override
	public int hashCode() {
	    return Objects.hash(nome, cognome);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Esperto cuoco = (Esperto) o;
		return Objects.equals(nome, cuoco.nome) && Objects.equals(cognome, cuoco.cognome);
	}
}
