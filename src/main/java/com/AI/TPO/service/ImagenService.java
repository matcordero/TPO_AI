package com.AI.TPO.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AI.TPO.entity.Imagen;

public interface ImagenService {
	public Iterable<Imagen> findAll();
	
	public Page<Imagen> findAll(Pageable pageable);
	
	public Optional<Imagen> findById(Integer numero);

	public Imagen save(Imagen imagen);
	
	public void deleteById(Integer numero);
}
