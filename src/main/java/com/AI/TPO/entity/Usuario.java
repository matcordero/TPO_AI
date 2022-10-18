package com.AI.TPO.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.AI.TPO.views.TipoPermiso;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity(name = "Usuario")
@Table(name = "usuarios")
public class Usuario {
	@Id
	@Column(name = "usuario")
	private String usuario;
	
	@JsonIgnore
	@Column(name = "contraseña")
	private String contraseña;
	
	@OneToOne
	@JoinColumn(name = "documento")
	@Cascade(CascadeType.ALL)
	@JsonIgnore
	private Persona persona;
	
	
	@JoinTable(name = "usuario_permiso",
			joinColumns = { @JoinColumn(name = "usuario", nullable = false)} ,
			inverseJoinColumns = {@JoinColumn(name = "id_permiso", nullable = false)})
	@ManyToMany()
	@Cascade(CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Permiso> permisos = new ArrayList<Permiso>();
	
	
	
	public Usuario() {
	}

	public Usuario(String usuario, String contraseña, Persona persona) {
		super();
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.persona = persona;
	}
	
	public Usuario(String usuario, String contraseña, Persona persona, List<Permiso> permisos) {
		super();
		this.usuario = usuario;
		this.contraseña = contraseña;
		this.persona = persona;
		this.permisos = permisos;
	}
	
	
	public boolean tieneEstePermiso(TipoPermiso tipoPermiso) {
		for (Permiso permiso: this.permisos) {
			if(permiso.getPermiso().equals(tipoPermiso)) {
				return true;
			}
		}
		return false;
			
	}
	public boolean tieneSoloEstePermiso(TipoPermiso tipoPermiso) {
		if(this.permisos.size()>1) {
			return false;
		}
		return tieneEstePermiso(tipoPermiso);
	}
	
	public void agregarPermiso(Permiso permiso) {
		if(!this.permisos.contains(permiso)) {
			this.permisos.add(permiso);
		}
	}
	public void removerPermiso(Permiso permiso) {
		this.permisos.remove(permiso);
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public List<Permiso> getPermisos() {
		return permisos;
	}

	public void setPermisos(List<Permiso> permisos) {
		this.permisos = permisos;
	}
	
	
}
