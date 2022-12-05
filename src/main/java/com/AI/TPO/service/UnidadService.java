package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Unidad;


public interface UnidadService {
	public Iterable<Unidad> findAll();
	
	public Page<Unidad> findAll(Pageable pageable);
	
	public Optional<Unidad> findById(Integer id);
	
	public List<Unidad> findByEdificioAndPisoAndNumero(Edificio edificio, String piso, String numero);
	
	public List<Unidad> findByPisoAndNumero(String piso, String numero);

	public Unidad save(Unidad unidad);
	
	public void deleteById(Integer id);
	
	public List<Unidad> findByDuenio(Persona p);
	
	public List<Unidad> findByInquilino(Persona p);
}
