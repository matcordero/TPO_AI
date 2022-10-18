package com.AI.TPO.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.repository.EdificioRepositorio;

@Service
public class EdificioServiceImplementado implements EdificioService{

	@Autowired
	private EdificioRepositorio edificioRepositorio;
	
	@Override
	@Transactional(readOnly = true)
	public Iterable<Edificio> findAll() {
		return edificioRepositorio.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Edificio> findAll(Pageable pageable) {
		return edificioRepositorio.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Edificio> findById(Integer codigo) {
		return edificioRepositorio.findById(codigo);
	}

	@Override
	@Transactional
	public Edificio save(Edificio edificio) {
		return edificioRepositorio.save(edificio);
	}

	@Override
	@Transactional
	public void deleteById(Integer codigo) {
		edificioRepositorio.deleteById(codigo);
		
	}

}
