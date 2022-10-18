package com.AI.TPO.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.AI.TPO.entity.Usuario;

public interface UsuarioService {
	public Iterable<Usuario> findAll();
	
	public Page<Usuario> findAll(Pageable pageable);
	
	public Optional<Usuario> findById(String usuario);

	public Usuario save(Usuario usuario);
	
	public void deleteById(String usuario);
}
