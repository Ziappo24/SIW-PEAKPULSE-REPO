package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

@Entity
public class Attivita {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	public String nome;
	
	public String urlImage;
	
	@Column(length=5000)
	public String descrizione;
	
	@Transient
	private MultipartFile immagine;
	
	@ManyToOne
	public Esperto esperto;
	

	@ManyToMany
	private List<Attrezzatura> attrezzatureUtilizzate;
	
	
	@OneToMany(mappedBy = "attivita")
	private List<Recensione> recensioni;

	 
	public Attivita() {
	    // inizializzazione della lista nel costruttore
	    this.attrezzatureUtilizzate = new ArrayList<>();
	 }
	 
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Esperto getEsperto() {
		return esperto;
	}

	public void setEsperto(Esperto esperto) {
		this.esperto = esperto;
	}

	public List<Attrezzatura> getAttrezzatureUtilizzate() {
		return attrezzatureUtilizzate;
	}

	public void setAttrezzatureUtilizzate(List<Attrezzatura> attrezzatureUtilizzate) {
		this.attrezzatureUtilizzate = attrezzatureUtilizzate;
	}

	public List<Recensione> getRecensioni() {
		return recensioni;
	}

	public void setRecensioni(List<Recensione> recensioni) {
		this.recensioni = recensioni;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Attivita other = (Attivita) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
	
}
