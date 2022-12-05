package com.AI.TPO.entity;

import com.AI.TPO.views.PersonaView;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;


@Entity(name = "Persona")
@Table(name = "personas")
public class Persona {
	@Id
	@Column(name = "documento")
	private String documento;
	@Column(name = "nombre")
	private String nombre;
	
	@OneToOne(mappedBy = "persona")
	@Cascade(CascadeType.ALL)
	@JsonIgnore
	private Usuario usuario;
	
	
	public Persona() {
		
	}
	
	public Persona(String documento, String nombre) {
		this.documento = documento;
		this.nombre = nombre;
	}

	public String getDocumento() {
		return documento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public PersonaView toView() {
		return new PersonaView(documento, nombre);
	}

	public void save() {
		
	}

	public void delete() {
		
	}
	
	
	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	@Override
	public String toString() {
		return "Persona [documento=" + documento + ", nombre=" + nombre + "]";
	}
	
}
