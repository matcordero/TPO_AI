package com.AI.TPO.views;

import java.util.List;

public class UnidadView {

	private int id;
	private String piso;
	private String numero;
	private boolean habitado;
	private EdificioView edificio;
	private List<PersonaView> duenios;
	private List<PersonaView> inquilinos;
	
	public UnidadView() {}

	public UnidadView(int id, String piso, String numero, boolean habitado, EdificioView edificio, List<PersonaView> duenios,List<PersonaView> inquilinos ) {
		this.id = id;
		this.piso = piso;
		this.numero = numero;
		this.habitado = habitado;
		this.edificio = edificio;
		this.duenios = duenios;
		this.inquilinos = inquilinos;
	}
	
	

	public List<PersonaView> getDuenios() {
		return duenios;
	}

	public void setDuenios(List<PersonaView> duenios) {
		this.duenios = duenios;
	}

	public List<PersonaView> getInquilinos() {
		return inquilinos;
	}

	public void setInquilinos(List<PersonaView> inquilinos) {
		this.inquilinos = inquilinos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPiso() {
		return piso;
	}

	public void setPiso(String piso) {
		this.piso = piso;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public boolean isHabitado() {
		return habitado;
	}

	public void setHabitado(boolean habitado) {
		this.habitado = habitado;
	}

	public EdificioView getEdificio() {
		return edificio;
	}

	public void setEdificio(EdificioView edificio) {
		this.edificio = edificio;
	}
	
	public String toString() {
		return piso + " " + numero;
	}
}
