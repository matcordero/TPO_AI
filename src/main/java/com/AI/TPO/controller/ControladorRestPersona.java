package com.AI.TPO.controller;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AI.TPO.entity.Persona;
import com.AI.TPO.service.PersonaService;



@RestController
@RequestMapping("/api/Persona")
public class ControladorRestPersona {
	
	@Autowired
	private PersonaService personaService;
	
	//Leer una persona
	@GetMapping(value = "/GetPersona/{documento}")
	public ResponseEntity<?> read(@PathVariable String documento){
		Optional<Persona> oPersona = personaService.findById(documento);
		if(oPersona.isPresent()) {
			return ResponseEntity.ok(oPersona);
		}
		return ResponseEntity.notFound().build();
	}
	
	//Leer una persona
		@GetMapping(value = "/GetPersonaNombre/{nombre}")
		public ResponseEntity<?> readNombre(@PathVariable String nombre){
			return ResponseEntity.status(HttpStatus.OK).body(personaService.findByNombre(nombre));
		}
	
	//Crear Persona
	@PostMapping(value = "/PostPersona",consumes = "application/json")
	public ResponseEntity<?> create2(@RequestBody Persona persona){
		System.out.println(persona);
		return ResponseEntity.status(HttpStatus.CREATED).body(personaService.save(persona));
	}
	
	//Obtener todas las personas
	@GetMapping(value = "/GetAllPersonas")
	public ResponseEntity<?> read(){
		List<Persona> personas = StreamSupport.stream(personaService.findAll().spliterator(), false).collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(personas);
	}
	
	//Obtener todoas las personas por mapeo
	@GetMapping(value = "/GetAllPersonasMapping/{paginas}/{cantidad}")
	public ResponseEntity<?> read(@PathVariable Integer paginas,@PathVariable Integer cantidad){
		//Pageable pagina = new Book();
		//pagina.
		Page<Persona> personas = personaService.findAll(PageRequest.of(paginas, cantidad));
		return ResponseEntity.status(HttpStatus.OK).body(personas);
	}
	
	
	
	
	
}
