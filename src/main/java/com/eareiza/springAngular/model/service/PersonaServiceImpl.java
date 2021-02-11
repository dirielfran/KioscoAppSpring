package com.eareiza.springAngular.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eareiza.springAngular.interfaces.IPersonaService;
import com.eareiza.springAngular.model.entity.Persona;
import com.eareiza.springAngular.model.repository.IPersonaRepository;

@Service
public class PersonaServiceImpl implements IPersonaService{
	
	@Autowired
	private IPersonaRepository personasRepo; 

	@Override
	@Transactional(readOnly = true)
	public List<Persona> findAll() {
		return personasRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Persona> findAll(Pageable pagina) {
		return personasRepo.findAll(pagina);
	}

	@Override
	@Transactional
	public Persona save(Persona persona) {
		return personasRepo.save(persona);
	}

	@Override
	@Transactional(readOnly = true)
	public Persona findById(Long id) {
		return personasRepo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		personasRepo.deleteById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Persona> findPersonaByNombre(String nombre) {
		return personasRepo.findByPerNombreContainingIgnoreCase(nombre);
	}


}
