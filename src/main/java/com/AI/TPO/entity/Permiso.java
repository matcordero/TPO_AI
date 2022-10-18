package com.AI.TPO.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.AI.TPO.convert.EnumPermisosConvert;
import com.AI.TPO.views.TipoPermiso;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity(name = "Permiso")
@Table(name = "permisos")
public class Permiso {
	@Id
	@Column(name = "id_permiso")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id_permiso;
	
	@Column(name = "descripcion")
	@Convert(converter = EnumPermisosConvert.class)
	private TipoPermiso permiso;

	
	public Permiso() {
		
	}


	public Integer getId_permiso() {
		return id_permiso;
	}


	public void setId_permiso(Integer id_permiso) {
		this.id_permiso = id_permiso;
	}


	public TipoPermiso getPermiso() {
		return permiso;
	}


	public void setPermiso(TipoPermiso permiso) {
		this.permiso = permiso;
	}
	
	
}
