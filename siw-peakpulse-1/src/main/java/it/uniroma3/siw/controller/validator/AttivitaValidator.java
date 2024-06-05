package it.uniroma3.siw.controller.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Attivita;
import it.uniroma3.siw.repository.AttivitaRepository;


@Component
public class AttivitaValidator implements Validator{
	
	@Autowired
	private AttivitaRepository attivitaRepository;

	@Override
	public void validate(Object o, Errors errors) {
		Attivita attivita = (Attivita)o;
		if (attivita.getNome()!=null && attivita.getEsperto()!=null 
				&& attivitaRepository.existsByNomeAndEsperto(attivita.getNome(), attivita.getEsperto())) {
			errors.reject("attivita.duplicate");
		}
	}
	
	@Override
	public boolean supports(Class<?> aClass) {
		return Attivita.class.equals(aClass);
	}
}
