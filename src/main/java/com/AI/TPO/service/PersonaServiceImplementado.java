package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.AI.TPO.entity.Persona;
import com.AI.TPO.repository.PersonaRepositorio;

@Service
public class PersonaServiceImplementado implements PersonaService{

	@Autowired
	private PersonaRepositorio personaRepositorio;
	
	@Override
	@Transactional(readOnly = true)
	public List<Persona> findAll() {
		return personaRepositorio.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Persona> findAll(Pageable pageable) {
		return personaRepositorio.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Persona> findById(String documento) {
		return personaRepositorio.findById(documento);
	}

	@Override
	@Transactional
	public Persona save(Persona persona) {
		return personaRepositorio.save(persona);
	}

	@Override
	@Transactional
	public void deleteById(String documento) {
		personaRepositorio.deleteById(documento);
	}

	@Override
	public List<Persona> findByNombre(String nombre) {
		return personaRepositorio.findByNombre(nombre);
	}


	

}
