package com.AI.TPO.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.AI.TPO.entity.Registro;
import com.AI.TPO.repository.RegistroRepositorio;

@Service
public class RegistroServiceImplementado implements RegistroService{

	@Autowired
	private RegistroRepositorio registroRepositorio;
	
	@Override
	public Iterable<Registro> findAll() {
		return registroRepositorio.findAll();
	}

	@Override
	public Page<Registro> findAll(Pageable pageable) {
		return registroRepositorio.findAll(pageable);
	}

	@Override
	public Optional<Registro> findById(Integer id) {
		return registroRepositorio.findById(id);
	}

	@Override
	public Registro save(Registro registro) {
		return registroRepositorio.save(registro);
	}

	@Override
	public void deleteById(Integer id) {
		registroRepositorio.deleteById(id);
	}

}
