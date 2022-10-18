package com.AI.TPO.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Reclamo;
import com.AI.TPO.entity.Unidad;
import com.AI.TPO.views.Estado;

@Repository
public interface ReclamoRepositorio extends JpaRepository<Reclamo,Integer>{
	List<Reclamo> findByEdificio(Edificio edificio);
	List<Reclamo> findByUnidad(Unidad unidad);
	List<Reclamo> findByUsuario(Persona usuario);
	List<Reclamo> findByEstado(Estado estado);
	
 }
