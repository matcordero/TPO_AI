package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.AI.TPO.entity.Permiso;
import com.AI.TPO.repository.PermisoRepositorio;
import com.AI.TPO.views.TipoPermiso;


@Service
public class PermisoServiceImplementado implements PermisoService{
	@Autowired
	private PermisoRepositorio permisoRepositorio;
	
	@Override
	public Iterable<Permiso> findAll() {
		return permisoRepositorio.findAll();
	}

	@Override
	public Page<Permiso> findAll(Pageable pageable) {
		return permisoRepositorio.findAll(pageable);
	}

	@Override
	public Optional<Permiso> findById(Integer id) {
		return permisoRepositorio.findById(id);
	}

	@Override
	public Permiso save(Permiso permiso) {
		return permisoRepositorio.save(permiso);
	}

	@Override
	public void deleteById(Integer id) {
		permisoRepositorio.deleteById(id);
	}

	@Override
	public List<Permiso> findByPermiso(TipoPermiso permiso) {
		return permisoRepositorio.findByPermiso(permiso);
	}

}
