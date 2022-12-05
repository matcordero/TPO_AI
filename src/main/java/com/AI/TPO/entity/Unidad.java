package com.AI.TPO.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.AI.TPO.convert.BooleanConvert;
import com.AI.TPO.exceptions.UnidadException;
import com.AI.TPO.views.EdificioView;
import com.AI.TPO.views.PersonaView;
import com.AI.TPO.views.TipoPermiso;
import com.AI.TPO.views.UnidadView;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity(name = "Unidad")
@Table(name = "unidades")
public class Unidad {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "identificador")
	private Integer id;
	
	@Column(name = "piso")
	private String piso;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "habitado")
	@Convert(converter = BooleanConvert.class)
	private boolean habitado;
	
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "codigoEdificio")
	@Cascade(CascadeType.ALL)
	private Edificio edificio;
	
	
	@JoinTable(name = "duenios",
			joinColumns = { @JoinColumn(name = "identificador", nullable = false)} ,
			inverseJoinColumns = {@JoinColumn(name = "documento", nullable = false)})
	@ManyToMany()
	@Cascade(CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Persona> duenios = new ArrayList<Persona>();
	
	
	
	@JoinTable(name = "inquilinos",
			joinColumns = { @JoinColumn(name = "identificador", nullable = false)} ,
			inverseJoinColumns = {@JoinColumn(name = "documento", nullable = false)})
	@ManyToMany()
	@Cascade(CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	private List<Persona> inquilinos = new ArrayList<Persona>();
	
	public Unidad() {
		
	}
	
	public Unidad(int id, String piso, String numero, Edificio edificio) {
		this.id = id;
		this.piso = piso;
		this.numero = numero;
		this.habitado = false;
		this.edificio = edificio;
	}
	
	public Unidad(String piso, String numero, Edificio edificio) {
		this.piso = piso;
		this.numero = numero;
		this.habitado = false;
		this.edificio = edificio;
	}
	
	public boolean tenesPersona(Persona personaB) {
		if(inquilinos.contains(personaB) || duenios.contains(personaB)) {
			return true;
		}
		return false;
	}
	public boolean tenesDuenio(Persona personaB) {
		if(duenios.contains(personaB)) {
			return true;
		}
		return false;
	}
	public boolean tenesInquilino(Persona personaB) {
		if(inquilinos.contains(personaB)) {
			return true;
		}
		return false;
	}
	
	
	public void transferir(Persona nuevoDuenio) {
		duenios = new ArrayList<Persona>();
		duenios.add(nuevoDuenio);
	}
	
	public void agregarDuenio(Persona duenio) {
		duenios.add(duenio);
	}
	
	public void alquilar(Persona inquilino) throws UnidadException {
		if(!this.habitado) {
			this.habitado = true;
			inquilinos = new ArrayList<Persona>();
			inquilinos.add(inquilino);
		}
		else
			throw new UnidadException("La unidad esta ocupada");
	}

	public void agregarInquilino(Persona inquilino) {
		inquilinos.add(inquilino);
	}
	
	public boolean estaHabitado() {
		return habitado;
	}
	
	public void liberar() {
		this.inquilinos = new ArrayList<Persona>();
		this.habitado = false;
	}
	
	public void habitar() throws UnidadException {
		if(this.habitado)
			throw new UnidadException("La unidad ya esta habitada");
		else
			this.habitado = true;
	}
	
	public Integer getId() {
		return id;
	}

	public String getPiso() {
		return piso;
	}

	public String getNumero() {
		return numero;
	}

	
	public Edificio getEdificio() {
		return edificio;
	}

	public List<Persona> getDuenios() {
		return duenios;
	}

	public List<Persona> getInquilinos() {
		return inquilinos;
	}

	public UnidadView toView() {
		EdificioView auxEdificio = edificio.toView();
		List<PersonaView> dueniosView = duenios.stream().map(x -> x.toView()).toList();
		List<PersonaView> inquilinosView = inquilinos.stream().map(x -> x.toView()).toList();
		return new UnidadView(id, piso, numero, habitado, auxEdificio,dueniosView,inquilinosView);
	}
	

	public boolean isHabitado() {
		return habitado;
	}

	public void setHabitado(boolean habitado) {
		this.habitado = habitado;
	}
	
}
