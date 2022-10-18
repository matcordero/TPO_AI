package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Reclamo;
import com.AI.TPO.entity.Unidad;
import com.AI.TPO.repository.ReclamoRepositorio;
import com.AI.TPO.views.Estado;

@Service
public class ReclamoServiceImplementado implements ReclamoService{
	@Autowired
	private ReclamoRepositorio reclamoRepositorio;
	
	@Override
	public Iterable<Reclamo> findAll() {
		return reclamoRepositorio.findAll();
	}

	@Override
	public Page<Reclamo> findAll(Pageable pageable) {
		return reclamoRepositorio.findAll(pageable);
	}

	@Override
	public Optional<Reclamo> findById(Integer numero) {
		return reclamoRepositorio.findById(numero);
	}

	@Override
	public Reclamo save(Reclamo reclamo) {
		return reclamoRepositorio.save(reclamo);
	}

	@Override
	public void deleteById(Integer codigo) {
		reclamoRepositorio.deleteById(codigo);
		
	}

	@Override
	public List<Reclamo> findByEdificio(Edificio edificio) {
		return reclamoRepositorio.findByEdificio(edificio);
	}

	@Override
	public List<Reclamo> findByUnidad(Unidad unidad) {
		return reclamoRepositorio.findByUnidad(unidad);
	}

	@Override
	public List<Reclamo> findByPersona(Persona persona) {
		return reclamoRepositorio.findByUsuario(persona);
	}

	@Override
	public List<Reclamo> findByEstado(Estado estado) {
		return reclamoRepositorio.findByEstado(estado);
	}

}
