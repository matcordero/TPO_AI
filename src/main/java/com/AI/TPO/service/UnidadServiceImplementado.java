package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Unidad;
import com.AI.TPO.repository.UnidadRepositorio;

@Service
public class UnidadServiceImplementado implements UnidadService{

	@Autowired
	private UnidadRepositorio unidadRepositorio;
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Unidad> findAll() {
		return unidadRepositorio.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Unidad> findAll(Pageable pageable) {
		return unidadRepositorio.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Unidad> findById(Integer id) {
		return unidadRepositorio.findById(id);
	}

	@Override
	@Transactional
	public Unidad save(Unidad unidad) {
		return unidadRepositorio.save(unidad);
	}

	@Override
	@Transactional
	public void deleteById(Integer id) {
		unidadRepositorio.deleteById(id);
		
	}
/*
	@Override
	public List<Unidad> findByCodigoAndPisoAndNumero(int codigo, String piso, String numero) {
		return unidadRepositorio.findByCodigoedificioAndPisoAndNumero(codigo, piso, numero);
		
	}*/
	@Override
	public List<Unidad> findByPisoAndNumero(String piso, String numero) {
		return unidadRepositorio.findByPisoAndNumero(piso, numero);
		
	}

	@Override
	public List<Unidad> findByEdificioAndPisoAndNumero(Edificio edificio, String piso, String numero) {
		return unidadRepositorio.findByEdificioAndPisoAndNumero(edificio, piso, numero);
	}

	@Override
	public List<Unidad> findByDuenio(Persona p) {
		return unidadRepositorio.findByDuenios(p);
	}

	@Override
	public List<Unidad> findByInquilino(Persona p) {
		return unidadRepositorio.findByInquilinos(p);
	}
	
}
