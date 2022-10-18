package com.AI.TPO.views;

import java.util.List;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Imagen;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Unidad;

public class ReclamoView {

	private int numero;
	private PersonaView usuario;
	private EdificioView edificio;
	private String ubicacion;
	private String descripcion;
	private UnidadView unidad;
	private Estado estado;
	private List<ImagenView> imagenes;
}
