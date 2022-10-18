package com.AI.TPO.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import com.AI.TPO.views.TipoPermiso;

@Component
@Converter(autoApply = true)
public class EnumPermisosConvert implements AttributeConverter<TipoPermiso,String>{

	Logger log = Logger.getLogger(EnumPermisosConvert.class.getSimpleName());
	
	//habitante, administrador, GestionPersonas, GestionReclamos, GestionEdificio, GestionUnidad
	
	@Override
	public String convertToDatabaseColumn(TipoPermiso attribute) {
		if (attribute !=null) {
			if(attribute.equals(TipoPermiso.habitante)) {
				return "habitante";
			}
			else if(attribute.equals(TipoPermiso.administrador)) {
				return "administrador";
			}
			else if(attribute.equals(TipoPermiso.gestionEdificios)) {
				return "gestionEdificios";
			}
			else if(attribute.equals(TipoPermiso.gestionPersonas)) {
				return "gestionPersonas";
			}
			else if(attribute.equals(TipoPermiso.gestionReclamos)) {
				return "gestionReclamos";
			}
			else if(attribute.equals(TipoPermiso.gestionUnidades)) {
				return "gestionUnidades";
			}
			else if(attribute.equals(TipoPermiso.gestionRegistros)) {
				return "gestionRegistros";
			}
		}
		return null;
	}

	@Override
	public TipoPermiso convertToEntityAttribute(String dbData) {
		if (dbData != null) {
			if(dbData.equals("habitante")) {
				return TipoPermiso.habitante;
			}
			else if(dbData.equals("administrador")) {
				return TipoPermiso.administrador;
			}
			else if(dbData.equals("gestionEdificios")) {
				return TipoPermiso.gestionEdificios;
			}
			else if(dbData.equals("gestionPersonas")) {
				return TipoPermiso.gestionPersonas;
			}
			else if(dbData.equals("gestionReclamos")) {
				return TipoPermiso.gestionReclamos;
			}
			else if(dbData.equals("gestionUnidades")) {
				return TipoPermiso.gestionUnidades;
			}
			else if(dbData.equals("gestionRegistros")) {
				return TipoPermiso.gestionRegistros;
			}
		}
		return null;
	}

}
