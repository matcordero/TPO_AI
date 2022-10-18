package com.AI.TPO.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AI.TPO.entity.Edificio;



public interface EdificioService {
	public Iterable<Edificio> findAll();
	
	public Page<Edificio> findAll(Pageable pageable);
	
	public Optional<Edificio> findById(Integer codigo);

	public Edificio save(Edificio edificio);
	
	public void deleteById(Integer codigo);
}
