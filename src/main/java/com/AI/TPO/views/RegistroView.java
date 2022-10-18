package com.AI.TPO.views;

import java.time.LocalDate;

import com.AI.TPO.entity.Usuario;

public class RegistroView {
	private Integer id_registro;
	private String descripcion;
	private String usuario;
	private LocalDate fecha;
	public RegistroView(Integer id_registro, String descripcion, String usuario, LocalDate fecha) {
		super();
		this.id_registro = id_registro;
		this.descripcion = descripcion;
		this.usuario = usuario;
		this.fecha = fecha;
	}
	
	public RegistroView() {
		
	}

	public Integer getId_registro() {
		return id_registro;
	}

	public void setId_registro(Integer id_registro) {
		this.id_registro = id_registro;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	
}
