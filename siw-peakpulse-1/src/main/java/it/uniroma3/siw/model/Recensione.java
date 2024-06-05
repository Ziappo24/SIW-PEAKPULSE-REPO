package it.uniroma3.siw.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Recensione {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(length = 5000)
	public String descrizione;
	
	private Integer numeroStelle;
	
	@ManyToOne
	private Esperto autore;
	

	@ManyToOne
	private Attivita attivita;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	
	public int getNumeroStelle() {
        return numeroStelle;
    }

    public void setNumeroStelle(int numeroStelle) {
        if (numeroStelle < 0 || numeroStelle > 5) {
            throw new IllegalArgumentException("Il numero di stelle deve essere compreso tra 0 e 5.");
        }
        this.numeroStelle = numeroStelle;
    }
    
	public Esperto getAutore() {
		return autore;
	}

	public void setAutore(Esperto autore) {
		this.autore = autore;
	}

	public Attivita getAttivita() {
		return attivita;
	}

	public void setAttivita(Attivita attivita) {
		this.attivita = attivita;
	}
    
}
