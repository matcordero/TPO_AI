package com.AI.TPO.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity(name = "Imagen")
@Table(name = "imagenes")
public class Imagen {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer numero;
	
	@Column(name = "path")
	private String direccion;
	
	@Column(name = "tipo")
	private String tipo;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "idReclamo")
	@Cascade(CascadeType.ALL)
	private Reclamo reclamo;
	
	public Imagen() {
		
	}
	public Imagen(String direccion, String tipo, Reclamo reclamo) {
		this.direccion = direccion;
		this.tipo = tipo;
		this.reclamo = reclamo;
	}
	
	public Imagen(Integer numero, String direccion, String tipo, Reclamo reclamo) {
		this.numero = numero;
		this.direccion = direccion;
		this.tipo = tipo;
		this.reclamo = reclamo;
	}

	public Integer getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public void save(int numeroReclamo) {
		
	}

	public Reclamo getReclamo() {
		return reclamo;
	}

	public void setReclamo(Reclamo reclamo) {
		this.reclamo = reclamo;
	}

	public void setNumero(Integer numero) {
		this.numero = numero;
	}
	
	
}
