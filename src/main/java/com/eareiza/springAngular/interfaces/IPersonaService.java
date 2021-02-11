package com.eareiza.springAngular.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eareiza.springAngular.model.entity.Persona;

public interface IPersonaService {
	
	public List<Persona> findAll();
	public Page<Persona> findAll(Pageable pagina);
	public Persona save(Persona persona);
	public Persona findById(Long id);
	public void delete(Long id);
	List<Persona> findPersonaByNombre(String nombre);

}
