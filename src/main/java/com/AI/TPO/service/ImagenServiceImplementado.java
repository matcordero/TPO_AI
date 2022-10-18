package com.AI.TPO.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.AI.TPO.entity.Imagen;
import com.AI.TPO.repository.ImagenRepositorio;

@Service
public class ImagenServiceImplementado implements ImagenService{
	@Autowired
	private ImagenRepositorio imagenRepositorio;
	
	@Override
	public Iterable<Imagen> findAll() {
		return imagenRepositorio.findAll();
	}

	@Override
	public Page<Imagen> findAll(Pageable pageable) {
		return imagenRepositorio.findAll(pageable);
	}

	@Override
	public Optional<Imagen> findById(Integer numero) {
		return imagenRepositorio.findById(numero);
	}

	@Override
	public Imagen save(Imagen imagen) {
		return imagenRepositorio.save(imagen);
	}

	@Override
	public void deleteById(Integer numero) {
		imagenRepositorio.deleteById(numero);
	}

}
