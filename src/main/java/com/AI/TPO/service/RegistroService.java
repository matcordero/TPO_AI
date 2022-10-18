package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AI.TPO.entity.Registro;

public interface RegistroService {

	public Iterable<Registro> findAll();
	
	public Page<Registro> findAll(Pageable pageable);
	
	public Optional<Registro> findById(Integer id);

	public Registro save(Registro registro);
	
	public void deleteById(Integer id);
}
