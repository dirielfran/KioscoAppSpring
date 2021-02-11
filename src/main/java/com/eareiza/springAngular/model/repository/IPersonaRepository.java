package com.eareiza.springAngular.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eareiza.springAngular.model.entity.Persona;

public interface IPersonaRepository  extends JpaRepository<Persona, Long>{
	//Con QueryMethod, busca donde nombre contenga el parametro y se ignore si es may. o min.
	public List<Persona> findByPerNombreContainingIgnoreCase(String nombre);

}
