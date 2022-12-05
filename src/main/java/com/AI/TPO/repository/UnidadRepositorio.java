package com.AI.TPO.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Unidad;

@Repository
public interface UnidadRepositorio extends JpaRepository<Unidad, Integer>{
	List<Unidad> findByEdificioAndPisoAndNumero(Edificio edificio, String piso, String numero);
	List<Unidad> findByPisoAndNumero(String piso, String numero);
	List<Unidad> findByDuenios(Persona duenios);
	List<Unidad> findByInquilinos(Persona inquilinos); 
}
