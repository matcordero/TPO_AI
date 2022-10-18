package com.AI.TPO.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AI.TPO.entity.Permiso;
import com.AI.TPO.views.TipoPermiso;

public interface PermisoService {
	public Iterable<Permiso> findAll();
	
	public Page<Permiso> findAll(Pageable pageable);
	
	public List<Permiso> findByPermiso(TipoPermiso permiso);
	
	public Optional<Permiso> findById(Integer id);

	public Permiso save(Permiso permiso);
	
	public void deleteById(Integer id);
}
