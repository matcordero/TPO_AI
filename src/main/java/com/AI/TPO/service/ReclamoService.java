package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Reclamo;
import com.AI.TPO.entity.Unidad;
import com.AI.TPO.views.Estado;

public interface ReclamoService {
	public Iterable<Reclamo> findAll();
	
	public Page<Reclamo> findAll(Pageable pageable);
	
	public Optional<Reclamo> findById(Integer numero);
	
	public List<Reclamo> findByEdificio(Edificio edificio);
	
	public List<Reclamo> findByEstado(Estado estado);
	
	public List<Reclamo> findByUnidad(Unidad unidad);

	public List<Reclamo> findByPersona(Persona persona);
	
	public Reclamo save(Reclamo reclamo);
	
	public void deleteById(Integer codigo);
}
