package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AI.TPO.entity.Persona;


public interface PersonaService {

	public List<Persona> findAll();
	
	public Page<Persona> findAll(Pageable pageable);
	
	public Optional<Persona> findById(String documento);
	
	public List<Persona> findByNombre(String nombre);

	public Persona save(Persona persona);
	
	public void deleteById(String documento);

}
