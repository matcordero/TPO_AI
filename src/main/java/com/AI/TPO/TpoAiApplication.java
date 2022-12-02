package com.AI.TPO;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Imagen;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Reclamo;
import com.AI.TPO.entity.Unidad;
import com.AI.TPO.repository.EdificioRepositorio;
import com.AI.TPO.repository.PersonaRepositorio;
import com.AI.TPO.repository.ReclamoRepositorio;
import com.AI.TPO.repository.UnidadRepositorio;
import com.AI.TPO.views.Estado;

@SpringBootApplication
public class TpoAiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TpoAiApplication.class, args);
	} 
	/*
	@Bean
	CommandLineRunner CrearPersonas(PersonaRepositorio personaRepositorio) {
		return args-> {
			Persona Matias = new Persona("DNI111", "Matias");
			personaRepositorio.save(Matias);
			Persona Fernando = new Persona("DNI222", "Fernando");
			personaRepositorio.save(Fernando);
			Persona Nicolas = new Persona("DNI333", "Nicolas");
			personaRepositorio.save(Nicolas);
			//----------------------------------------------------
			Optional<Persona> OPersona = personaRepositorio.findById("DNI111");
			if(OPersona.isPresent()) {
				Persona persona = OPersona.get();
				System.out.println("Persona -> Nombre: " + persona.getNombre() + " Documento: " + persona.getDocumento());
			}
		};
	}*/
	
	/*
	@Bean
	CommandLineRunner CrearEdificioYUnidad(PersonaRepositorio personaRepositorio,EdificioRepositorio edificioRepositorio) {
		return args-> {
			Optional<Persona> OMatias = personaRepositorio.findById("DNI111");
			Optional<Persona> ONicolas = personaRepositorio.findById("DNI222");
			Optional<Persona> OFernando = personaRepositorio.findById("DNI333");
			if(OMatias.isPresent() && ONicolas.isPresent() & OFernando.isPresent()) {
				Persona Matias = OMatias.get();
				Persona Nicolas = ONicolas.get();
				Persona Fernando = OFernando.get();
				Edificio edificio = new Edificio(0,"Mega Edificio","Capital");
				
				Unidad unidad = new Unidad(0,"1","1",edificio);
				unidad.setHabitado(true);
				unidad.agregarDuenio(Matias);
				unidad.agregarInquilino(Fernando);
				unidad.agregarInquilino(Nicolas);
				
				edificio.agregarUnidad(unidad);
				edificioRepositorio.save(edificio);
			}
		};
	}*/
	
	/*
	@Bean
	CommandLineRunner TraerEdificios(EdificioRepositorio edificioRepositorio) {
		return args-> {
			Optional<Edificio> oEdificio = edificioRepositorio.findById(10);
			if(oEdificio.isPresent()) {
				Edificio edificio = oEdificio.get();
				System.out.println("Edificio -> Nombre: "+edificio.getNombre());
				List<Unidad> unidades = edificio.getUnidades();
				System.err.println("Cantidad Unidades -> "+unidades.size());
				System.out.println(edificio.habilitados().size());
				for(Unidad u:unidades) {
					System.out.println("Numero de Unidad: " + u.getNumero());
					for(Persona p:u.getDuenios()) {
						System.out.println("Duenios -> " + p.getNombre());
					}
					for(Persona p:u.getInquilinos()) {
						System.out.println("Inquilinos -> " + p.getNombre());
					}
				}
			}

		};
	}*/
	
	/*
	@Bean
	CommandLineRunner AgregarImagenAlReclamo(ReclamoRepositorio reclamoRepositorio) {
		return args-> {
            Optional<Reclamo> OReclamo = reclamoRepositorio.findById(1);
            if(OReclamo.isPresent()) {
            	Reclamo reclamo = OReclamo.get();
            	System.out.println("Reclamo Estado->" + reclamo.getEstado());
            	System.out.println("Reclamo Descripcion -> " + reclamo.getDescripcion());
            	reclamo.agregarImagen("y","y");
            	reclamoRepositorio.save(reclamo);
            }
		};
	}*/
	
	/*
	@Bean
	CommandLineRunner TraerReclamo(ReclamoRepositorio reclamoRepositorio) {
		return args-> {
            Optional<Reclamo> OReclamo = reclamoRepositorio.findById(1);
            if(OReclamo.isPresent()) {
            	Reclamo reclamo = OReclamo.get();
            	System.out.println("Reclamo Estado->" + reclamo.getEstado());
            	System.out.println("Reclamo Descripcion -> " + reclamo.getDescripcion());
            	for(Imagen i:reclamo.getImagenes()) {
            		System.out.println("Imagen -> "+i.getDireccion() +" " +i.getTipo());
            	}
            }
		};
	}
	*/
	
}
