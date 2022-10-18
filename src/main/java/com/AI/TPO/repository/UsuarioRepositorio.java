package com.AI.TPO.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AI.TPO.entity.Usuario;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
	
}
