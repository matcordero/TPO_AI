package com.AI.TPO.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.AI.TPO.entity.Usuario;
import com.AI.TPO.repository.UsuarioRepositorio;

@Service
public class UsuarioServiceImplementado implements UsuarioService{

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;
	
	@Override
	public Iterable<Usuario> findAll() {
		return usuarioRepositorio.findAll();
	}

	@Override
	public Page<Usuario> findAll(Pageable pageable) {
		return usuarioRepositorio.findAll(pageable);
	}

	@Override
	public Optional<Usuario> findById(String usuario) {
		return usuarioRepositorio.findById(usuario);
	}

	@Override
	public Usuario save(Usuario usuario) {
		return usuarioRepositorio.save(usuario);
	}

	@Override
	public void deleteById(String usuario) {
		usuarioRepositorio.deleteById(usuario);
	}

}
