package com.AI.TPO.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AI.TPO.entity.Permiso;
import com.AI.TPO.views.TipoPermiso;

@Repository
public interface PermisoRepositorio extends JpaRepository<Permiso, Integer> {
	List<Permiso> findByPermiso(TipoPermiso permiso);
}
