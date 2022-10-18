package com.AI.TPO.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AI.TPO.entity.Persona;

@Repository
public interface PersonaRepositorio extends JpaRepository<Persona, String>{
	List<Persona> findByNombre(String nombre);
}
