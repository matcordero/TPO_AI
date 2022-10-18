package com.AI.TPO.convert;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.jboss.logging.Logger;

@Converter(autoApply = true)
public class BooleanConvert implements AttributeConverter<Boolean,String>{

	Logger log = Logger.getLogger(BooleanConvert.class.getSimpleName());
	
	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		if (attribute !=null) {
			if(attribute) {
				return "Y";
			} else {
				return "N";
			}
		}
		return null;
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		if (dbData != null) {
			return dbData.equals("Y");
		}
		return null;
	}
	

}
