package com.AI.TPO.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import com.AI.TPO.views.Estado;

@Component
@Converter(autoApply = true)
public class EnumConvert implements AttributeConverter<Estado,String>{

	Logger log = Logger.getLogger(EnumConvert.class.getSimpleName());
	
	//nuevo, abierto, enProceso, desestimado, anulado, terminado 
	
	@Override
	public String convertToDatabaseColumn(Estado attribute) {
		if (attribute !=null) {
			if(attribute.equals(Estado.abierto)) {
				return "abierto";
			}
			else if(attribute.equals(Estado.nuevo)) {
				return "nuevo";
			}
			else if(attribute.equals(Estado.enProceso)) {
				return "enProceso";
			}
			else if(attribute.equals(Estado.desestimado)) {
				return "desestimado";
			}
			else if(attribute.equals(Estado.anulado)) {
				return "anulado";
			}
			else if(attribute.equals(Estado.terminado)) {
				return "terminado";
			}
		}
		return null;
	}

	@Override
	public Estado convertToEntityAttribute(String dbData) {
		if (dbData != null) {
			if(dbData.equals("abierto")) {
				return Estado.abierto;
			}
			else if(dbData.equals("nuevo")) {
				return Estado.nuevo;
			}
			else if(dbData.equals("enProceso")) {
				return Estado.enProceso;
			}
			else if(dbData.equals("desestimado")) {
				return Estado.desestimado;
			}
			else if(dbData.equals("anulado")) {
				return Estado.anulado;
			}
			else if(dbData.equals("terminado")) {
				return Estado.terminado;
			}
		}
		return null;
	}

}
