package com.AI.TPO.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.AI.TPO.convert.EnumConvert;
import com.AI.TPO.convert.EnumPermisosConvert;
import com.AI.TPO.entity.Edificio;
import com.AI.TPO.entity.Imagen;
import com.AI.TPO.entity.Permiso;
import com.AI.TPO.entity.Persona;
import com.AI.TPO.entity.Reclamo;
import com.AI.TPO.entity.Registro;
import com.AI.TPO.entity.Unidad;
import com.AI.TPO.entity.Usuario;
import com.AI.TPO.exceptions.EdificioException;
import com.AI.TPO.exceptions.UnidadException;
import com.AI.TPO.service.EdificioService;
import com.AI.TPO.service.ImagenService;
import com.AI.TPO.service.PermisoService;
import com.AI.TPO.service.PersonaService;
import com.AI.TPO.service.ReclamoService;
import com.AI.TPO.service.RegistroService;
import com.AI.TPO.service.UnidadService;
import com.AI.TPO.service.UsuarioService;
import com.AI.TPO.views.EdificioView;
import com.AI.TPO.views.Estado;
import com.AI.TPO.views.PersonaView;
import com.AI.TPO.views.ReclamoView;
import com.AI.TPO.views.TipoPermiso;
import com.AI.TPO.views.UnidadView;

@CrossOrigin
@RestController
@RequestMapping("/TPOAPI/Controller")
public class ControladorRest {
	@Autowired
	private EdificioService edificioService; 
	@Autowired
	private PersonaService personaService;
	@Autowired
	private UnidadService unidadService;
	@Autowired
	private ReclamoService reclamoService;
	@Autowired
	private ImagenService imagenService;
	@Autowired
	private EnumConvert enumConvert;
	@Autowired
	private EnumPermisosConvert enumPermisosConvert;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private PermisoService permisoService;
	@Autowired
	private RegistroService registroService;
	
	//verificacion
	private class Validacion{
		public String mensaje;
		public Usuario usuario;
		public Validacion(String mensaje, Usuario usuario) {
			super();
			this.mensaje = mensaje;
			this.usuario = usuario;
		}
		public String getMensaje() {
			return mensaje;
		}
		public Usuario getUsuario() {
			return usuario;
		}
		
	}
	
	private Usuario usuarioExiste(String Usuario,String Contraseña) {
		Optional<Usuario> oUsuario= usuarioService.findById(Usuario);
		if(oUsuario.isPresent()) {
			Usuario usuario = oUsuario.get();
			if(usuario.getContraseña().equals(Contraseña)) {
				return usuario;
				
			}
		}
		return null;
	}
	
	private boolean tienePermiso(Usuario usuario, TipoPermiso... PermisosRequeridos) {
		
		for(TipoPermiso permiso:PermisosRequeridos) {
			if(usuario.tieneEstePermiso(permiso)) {
				return true;
			}
		}
		return false;
	}
	
	private Validacion usuarioTieneAcceso(String usuario,String contraseña,TipoPermiso... permisosRequeridos) {
		Usuario UsuarioIngresado = usuarioExiste(usuario, contraseña);
			if(UsuarioIngresado==null) {
				return new Validacion("Usuario o Contraseña del empleado incorrecto",null);
			}
			else {
				if (tienePermiso(UsuarioIngresado,permisosRequeridos)) {
					return new Validacion("Ok",UsuarioIngresado);
				}
			}
			return new Validacion("No tienes los permisos para esto",null);
	}
	//Registro
	@GetMapping(value = "/GetAllRegistros")
	public ResponseEntity<?> getAllRegistros(@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionRegistros);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		registroService.save(new Registro("Solicito ver todos los registros",estado.getUsuario(),LocalDate.now()));
		return ResponseEntity.status(HttpStatus.OK).body(registroService.findAll());
	}
	
	
	//Edificio
	@CrossOrigin
	@GetMapping(value = "/GetEdificioByID/{id}")
	public ResponseEntity<?> getEdificioByID(@PathVariable Integer id){
		Optional<Edificio> oEdificio = edificioService.findById(id);
		if(oEdificio.isPresent()) {
			List<Edificio> edificios= new ArrayList<>();
			edificios.add(oEdificio.get());
			return ResponseEntity.status(HttpStatus.OK).body(edificios);
		}
		return ResponseEntity.notFound().build();
	}
	
	@CrossOrigin
	@GetMapping(value = "/GetEdificiosByUsuario/{dni}")
	public ResponseEntity<?> getEdificioByID(@PathVariable String dni){
		Optional<Persona> oPersona = personaService.findById(dni);
		if(oPersona.isPresent()) {
			Persona persona = oPersona.get();
			List<EdificioView> edificios = new ArrayList<>();
			List<Unidad> unidadDuenios = unidadService.findByDuenio(persona);
			List<Unidad> unidadInquilino = unidadService.findByInquilino(persona);
			edificios.addAll(unidadDuenios.stream().map(x -> x.getEdificio().toView()).toList());
			edificios.addAll(unidadInquilino.stream().map(x -> x.getEdificio().toView()).toList());
			Set<EdificioView> setEdificios = new HashSet<>(edificios);
			return ResponseEntity.status(HttpStatus.OK).body(setEdificios);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping(value = "/GetAllEdificios")
	public ResponseEntity<?> getAllEdificios(@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionEdificios);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		List<Edificio> edificios = StreamSupport.stream(edificioService.findAll().spliterator(), false).collect(Collectors.toList());
		List<EdificioView> edificiosView = edificios.stream().map(x -> x.toView()).collect(Collectors.toList());
		registroService.save(new Registro("Solicito ver todos los edificios",estado.getUsuario(),LocalDate.now()));
		return ResponseEntity.status(HttpStatus.OK).body(edificiosView);
	}
	
	@GetMapping(value = "/GetUnidadesByEdificio/{codigo}")
	public ResponseEntity<?> getUnidadesByEdificio(@PathVariable Integer codigo,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionEdificios);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> Oedificio = edificioService.findById(codigo);
		if(Oedificio.isPresent()) {
			Edificio edificio = Oedificio.get();
			List<Unidad> unidades = edificio.getUnidades();
			List<UnidadView> views = unidades.stream().map(x -> x.toView()).collect(Collectors.toList());
			registroService.save(new Registro("Solicito las Unidades del edificion" +codigo,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(views);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping(value = "/GetHabilitadosByEdificio/{codigo}")
	public ResponseEntity<?> getHabilitadosByEdificio(@PathVariable Integer codigo,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionEdificios);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> Oedificio = edificioService.findById(codigo);
		if(Oedificio.isPresent()) {
			Edificio edificio = Oedificio.get();
			List<Persona> personas = new ArrayList<>(edificio.habilitados());
			List<PersonaView> views = personas.stream().map(x -> x.toView()).collect(Collectors.toList());
			registroService.save(new Registro("Solicito los Habilitados del edificion" +codigo,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(views);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping(value = "/GetDueniosByEdificio/{codigo}")
	public ResponseEntity<?> getDueniosByEdificio(@PathVariable Integer codigo,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionEdificios);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> Oedificio = edificioService.findById(codigo);
		if(Oedificio.isPresent()) {
			Edificio edificio = Oedificio.get();
			List<Persona> personas = new ArrayList<>(edificio.duenios());
			List<PersonaView> views = personas.stream().map(x -> x.toView()).collect(Collectors.toList());
			registroService.save(new Registro("Solicito los Dueños del edificion" +codigo,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(views);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping(value = "/GetHabitantesByEdificio/{codigo}")
	public ResponseEntity<?> getHabitantesByEdificio(@PathVariable Integer codigo,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionEdificios);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> Oedificio = edificioService.findById(codigo);
		if(Oedificio.isPresent()) {
			Edificio edificio = Oedificio.get();
			List<Persona> personas = new ArrayList<>(edificio.habitantes());
			List<PersonaView> views = personas.stream().map(x -> x.toView()).collect(Collectors.toList());
			registroService.save(new Registro("Solicito los Habitantes del edificion" +codigo,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(views);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping(value = "/PostCrearEdificio")
	public ResponseEntity<?> postCrearEdificio(@RequestParam("nombre") String nombre,@RequestParam("direccion") String direccion,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionEdificios);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		registroService.save(new Registro("Creo el edificion " + nombre,estado.getUsuario(),LocalDate.now()));
		return ResponseEntity.status(HttpStatus.OK).body(edificioService.save(new Edificio(nombre,direccion)));
	}
	
	
	//------------------------------------------------------------------------
	//Unidad
	@PostMapping(value = "/PostCrearUnidad")
	public ResponseEntity<?> postCrearUnidad(@RequestParam("piso") String piso,@RequestParam("numero") String numero,@RequestParam("codigo") Integer codigo,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionEdificios);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigo);
		if(oEdificio.isPresent()) {
			registroService.save(new Registro("Creo la unidad en el edificio " + codigo,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(unidadService.save(new Unidad(piso,numero,oEdificio.get())));
		}
		return ResponseEntity.notFound().build();
	}
	@CrossOrigin
	@GetMapping(value = "/GetUnidadByDuenios/{id}")
	public ResponseEntity<?> getUnidadByDuenios(@PathVariable String id){
		Optional<Persona> oPersona = personaService.findById(id);
		if(oPersona.isPresent()) {
			Persona persona = oPersona.get();
			return ResponseEntity.status(HttpStatus.OK).body(unidadService.findByDuenio(persona));
		}
		return ResponseEntity.badRequest().build();
	}
	@CrossOrigin
	@GetMapping(value = "/GetUnidadByInquilino/{id}")
	public ResponseEntity<?> getUnidadByInquilino(@PathVariable String id){
		Optional<Persona> oPersona = personaService.findById(id);
		if(oPersona.isPresent()) {
			Persona persona = oPersona.get();
			return ResponseEntity.status(HttpStatus.OK).body(unidadService.findByInquilino(persona));
		}
		return ResponseEntity.badRequest().build();
	}
	
	
	@GetMapping(value = "/GetDueniosByUnidad/{id}")
	public ResponseEntity<?> getDueniosByUnidad(@PathVariable Integer id,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionUnidades);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Unidad> oUnidad = unidadService.findById(id);
		if(oUnidad.isPresent()) {
			Unidad unidad = oUnidad.get();
			List<Persona> personas = new ArrayList<>(unidad.getDuenios());
			List<PersonaView> views = personas.stream().map(x -> x.toView()).collect(Collectors.toList());
			registroService.save(new Registro("Solicito los Dueñios de la Unidad" + id,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(views);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping(value = "/GetInquilinosByUnidad/{id}")
	public ResponseEntity<?> getInquilinosByUnidad(@PathVariable Integer id,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionUnidades);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Unidad> oUnidad = unidadService.findById(id);
		if(oUnidad.isPresent()) {
			Unidad unidad = oUnidad.get();
			List<Persona> personas = new ArrayList<>(unidad.getInquilinos());
			List<PersonaView> views = personas.stream().map(x -> x.toView()).collect(Collectors.toList());
			registroService.save(new Registro("Solicito los Inquilinos de la Unidad" + id,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(views);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping(value = "/PutTransferirUnidad/{codigoEdificio}/{piso}/{numero}/{documento}")
	public ResponseEntity<?> putTransferirUnidad(@PathVariable Integer codigoEdificio,@PathVariable String piso,
			@PathVariable String numero,@PathVariable String documento,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionUnidades);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigoEdificio);
		if(oEdificio.isPresent()) {
			List<Unidad> unidades = unidadService.findByEdificioAndPisoAndNumero(oEdificio.get(), piso, numero);
			Optional<Persona> oPersona = personaService.findById(documento);
			if(oPersona.isPresent()) {
				Persona persona = oPersona.get();
				for(Unidad u:unidades) {
					u.transferir(persona);
					unidadService.save(u);
				}
				registroService.save(new Registro("Transfirio la unidad " +numero+ " a " + persona.getNombre(),estado.getUsuario(),LocalDate.now()));
				return ResponseEntity.status(HttpStatus.OK).body(unidades);
			}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.notFound().build();
	}
	@PutMapping(value = "/PutAgregarDuenioUnidad/{codigoEdificio}/{piso}/{numero}/{documento}")
	public ResponseEntity<?> putAgregarDuenioUnidad(@PathVariable Integer codigoEdificio,@PathVariable String piso,
			@PathVariable String numero,@PathVariable String documento, @RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionUnidades);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigoEdificio);
		if(oEdificio.isPresent()) {
			List<Unidad> unidades = unidadService.findByEdificioAndPisoAndNumero(oEdificio.get(), piso, numero);
			Optional<Persona> oPersona = personaService.findById(documento);
			if(oPersona.isPresent()) {
				Persona persona = oPersona.get();
				for(Unidad u:unidades) {
					u.agregarDuenio(persona);
					unidadService.save(u);
				}
				registroService.save(new Registro("Agrego como Dueñio de la unidad " +numero+ " a " + persona.getNombre(),estado.getUsuario(),LocalDate.now()));
				return ResponseEntity.status(HttpStatus.OK).body(unidades);
			}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.notFound().build();
	}
	@PutMapping(value = "/PutAlquilarUnidad/{codigoEdificio}/{piso}/{numero}/{documento}")
	public ResponseEntity<?> putAlquilarUnidad(@PathVariable Integer codigoEdificio,@PathVariable String piso,
			@PathVariable String numero,@PathVariable String documento,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionUnidades);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigoEdificio);
		if(oEdificio.isPresent()) {
			List<Unidad> unidades = unidadService.findByEdificioAndPisoAndNumero(oEdificio.get(), piso, numero);
			Optional<Persona> oPersona = personaService.findById(documento);
			if(oPersona.isPresent()) {
				Persona persona = oPersona.get();
				for(Unidad u:unidades) {
					try {
						u.alquilar(persona);
						unidadService.save(u);
					} catch (UnidadException e) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
					}
				}
				registroService.save(new Registro("Alquilo la unidad " +numero+ " a " + persona.getNombre(),estado.getUsuario(),LocalDate.now()));
				return ResponseEntity.status(HttpStatus.OK).body(unidades);
			}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping(value = "/PutAgregarInquilinoUnidad/{codigoEdificio}/{piso}/{numero}/{documento}")
	public ResponseEntity<?> putAgregarInquilinoUnidad(@PathVariable Integer codigoEdificio,@PathVariable String piso,
			@PathVariable String numero,@PathVariable String documento,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionUnidades);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigoEdificio);
		if(oEdificio.isPresent()) {
			List<Unidad> unidades = unidadService.findByEdificioAndPisoAndNumero(oEdificio.get(), piso, numero);
			Optional<Persona> oPersona = personaService.findById(documento);
			if(oPersona.isPresent()) {
				Persona persona = oPersona.get();
				for(Unidad u:unidades) {
					u.agregarInquilino(persona);
					unidadService.save(u);
				}
				registroService.save(new Registro("Agrego como inquilino a la unidad " +numero+ " a " + persona.getNombre(),estado.getUsuario(),LocalDate.now()));
				return ResponseEntity.status(HttpStatus.OK).body(unidades);
			}
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.notFound().build();
	}
	@PutMapping(value = "/PutLiberarUnidad/{codigoEdificio}/{piso}/{numero}")
	public ResponseEntity<?> putLiberarUnidad(@PathVariable Integer codigoEdificio,@PathVariable String piso,
			@PathVariable String numero, @RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionUnidades);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigoEdificio);
		if(oEdificio.isPresent()) {
			List<Unidad> unidades = unidadService.findByEdificioAndPisoAndNumero(oEdificio.get(), piso, numero);
			for(Unidad u:unidades) {
				u.liberar();
				unidadService.save(u);
			}
			registroService.save(new Registro("Libero la unidad " +numero,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(unidades);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping(value = "/PutHabitarUnidad/{codigoEdificio}/{piso}/{numero}")
	public ResponseEntity<?> putHabitarUnidad(@PathVariable Integer codigoEdificio,@PathVariable String piso,
			@PathVariable String numero, @RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionUnidades);
				if (!estado.getMensaje().equals("Ok")){
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
				}
		Optional<Edificio> oEdificio = edificioService.findById(codigoEdificio);
		if(oEdificio.isPresent()) {
			List<Unidad> unidades = unidadService.findByEdificioAndPisoAndNumero(oEdificio.get(), piso, numero);
			for(Unidad u:unidades) {
				try {
					u.habitar();
				} catch (UnidadException e) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
				}
				unidadService.save(u);
			}
			registroService.save(new Registro("Cambio estado a Habitar a la unidad " + numero,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(unidades);
		}
		return ResponseEntity.notFound().build();
	}
	@CrossOrigin
	@GetMapping(value = "/GetUnidadByID/{id}")
	public ResponseEntity<?> getUnidadByID(@PathVariable Integer id){
		Optional<Unidad> oUnidad = unidadService.findById(id);
		if(oUnidad.isPresent()) {
			List<UnidadView> unidades= new ArrayList<>();
			unidades.add(oUnidad.get().toView());
			return ResponseEntity.status(HttpStatus.OK).body(unidades);
		}
		
		return ResponseEntity.notFound().build();
	}
	
	
	//---------------------------------------------------------
	//Persona
	@GetMapping(value = "/GetBuscarPersona/{documento}")
	public ResponseEntity<?> read(@PathVariable String documento,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionPersonas);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Persona> oPersona = personaService.findById(documento);
		if(oPersona.isPresent()) {
			registroService.save(new Registro("Solicito a la persona " + oPersona.get().getNombre(),estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.ok(oPersona);
		}
		return ResponseEntity.notFound().build();
	}
	
	@CrossOrigin
	@PostMapping(value = "/Login")
	public ResponseEntity<?> login(@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Optional<Usuario> oUsuario = usuarioService.findById(usuarioEmpleado);
		if(oUsuario.isPresent() && oUsuario.get().getContraseña().equals(contraseñaEmpleado)) {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(oUsuario.get());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	@PostMapping(value = "/PostCrearPersona")
	public ResponseEntity<?> postCrearPersona(@RequestParam("documento") String documento,@RequestParam("nombre") String nombre,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionPersonas);
		System.out.println(usuarioEmpleado);
		System.out.println(contraseñaEmpleado);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		registroService.save(new Registro("Creo a la persona " + nombre,estado.getUsuario(),LocalDate.now()));
		return ResponseEntity.status(HttpStatus.CREATED).body(personaService.save(new Persona(documento,nombre)));
	}
	
	@DeleteMapping(value = "/DeleteEliminarPersona/{documento}")
	public ResponseEntity<?> deleteEliminarPersona(@PathVariable String documento,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionPersonas);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		if(personaService.findById(documento).isPresent()) {
			personaService.deleteById(documento);
			registroService.save(new Registro("Elimino a la Persona con Documento " + documento,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body("Persona con documento: "+documento+" fue eliminado");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La Persona no existe");
	}
	@GetMapping(value = "/GetPermisos/{documento}")
	public ResponseEntity<?> GetPermisos(@PathVariable String documento,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionPersonas);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Persona> oPersona = personaService.findById(documento);
		if(oPersona.isPresent()) {
			Persona persona = oPersona.get();
			if(persona.getUsuario()==null) {
				return ResponseEntity.status(HttpStatus.OK).body("La Persona no tiene usuario registrado");
			}
			else {
				List<Permiso> permisos = persona.getUsuario().getPermisos();
				if(permisos.size()==0) {
					return ResponseEntity.status(HttpStatus.OK).body("La Persona no tiene permisos");
				}
				else {
					registroService.save(new Registro("Solicito los permisos del usuario con documento "+documento,estado.getUsuario(),LocalDate.now()));
					return ResponseEntity.ok(permisos);
				}
			}
		}
		return ResponseEntity.notFound().build();
	}
	@PostMapping(value = "/PostRegistrarUsuario")
	public ResponseEntity<?> PostRegistrarUsuario(@RequestParam("documento") String documento,@RequestParam("usuario") String usuario,@RequestParam("contraseña") String contraseña,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionPersonas);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Persona> oPersona = personaService.findById(documento);
		if(oPersona.isPresent()) {
			Persona persona = oPersona.get();
			if (persona.getUsuario()==null) {
				persona.setUsuario(new Usuario(usuario,contraseña,persona));
				registroService.save(new Registro("Registro al usuario " + usuario,estado.getUsuario(),LocalDate.now()));
				return ResponseEntity.status(HttpStatus.CREATED).body(personaService.save(persona));
			}
			return ResponseEntity.status(HttpStatus.OK).body("La Persona ya tiene usuario registrado");
		}
		return ResponseEntity.status(HttpStatus.OK).body("La Persona no existe");
	}
	@PutMapping(value = "/PutAgregarPermiso")
	public ResponseEntity<?> PutAgregarPermiso(@RequestParam("usuario") String usuarioMod,@RequestParam("permiso") String permiso,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionPersonas);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Usuario> oUsuario= usuarioService.findById(usuarioMod);
		List<Permiso> permisos = permisoService.findByPermiso(enumPermisosConvert.convertToEntityAttribute(permiso));
		if(oUsuario.isPresent() && permisos.size()>0) {
			Usuario usuario = oUsuario.get();
			Permiso permisoAgregar = permisos.get(0);
			usuario.agregarPermiso(permisoAgregar);
			registroService.save(new Registro("Se agrego el permiso "+permiso +" al usuario " +usuario.getUsuario(),estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.save(usuario));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario o Permiso no existen");
			
		
	}
	@PutMapping(value = "/PutQuitarPermiso")
	public ResponseEntity<?> PutQuitarPermiso(@RequestParam("usuario") String usuarioMod,@RequestParam("permiso") String permiso,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionPersonas);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Usuario> oUsuario= usuarioService.findById(usuarioMod);
		List<Permiso> permisos = permisoService.findByPermiso(enumPermisosConvert.convertToEntityAttribute(permiso));
		if(oUsuario.isPresent() && permisos.size()>0) {
			Usuario usuario = oUsuario.get();
			Permiso permisoSacar = permisos.get(0);
			usuario.removerPermiso(permisoSacar);
			registroService.save(new Registro("Quito el permiso "+permiso +" al usuario " +usuario.getUsuario(),estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(usuarioService.save(usuario));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario o Permiso no existen");
	}
	
	
	//-----------------------------------------------------------
	//Reclamo
	@CrossOrigin
	@PostMapping(value = "/ReclamosByEdificio/{codigo}")
	public ResponseEntity<?> getReclamosByEdificio(@PathVariable Integer codigo,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos,TipoPermiso.habitante);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigo);
		if(oEdificio.isPresent()) {
			Edificio edificio = oEdificio.get();
			if(estado.getUsuario().tieneSoloEstePermiso(TipoPermiso.habitante)) {
				if(!edificio.tenesPersona(estado.getUsuario().getPersona())) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sos habitante de este edificio");
				}
			}
			List<Reclamo> reclamos = reclamoService.findByEdificio(edificio);
			registroService.save(new Registro("Solicito los Reclamos del edificio " + codigo,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(reclamos);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El Edificio no existe");
	}
	
	@GetMapping(value = "/GetReclamosByEstado/{estadobuscado}")
	public ResponseEntity<?> getReclamosByEstado(@PathVariable String estadobuscado,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		List<Reclamo> Reclamos = reclamoService.findByEstado(enumConvert.convertToEntityAttribute(estadobuscado));
		registroService.save(new Registro("Solicito los Reclamos con estado " + estadobuscado,estado.getUsuario(),LocalDate.now()));
		return ResponseEntity.status(HttpStatus.OK).body(Reclamos);
	}
	
	@PostMapping(value = "/GetReclamosByUnidad/{id}")
	public ResponseEntity<?> getReclamosByUnidad(@PathVariable Integer id,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos,TipoPermiso.habitante);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		
		Optional<Unidad> oUnidad = unidadService.findById(id);
		if(oUnidad.isPresent()) {
			Unidad unidad = oUnidad.get();
			List<Reclamo> reclamos = reclamoService.findByUnidad(unidad);
			registroService.save(new Registro("Solicito los reclamos de la unidad " + id,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(reclamos);
		}
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("La Unidad no existe");
	}
	
	@GetMapping(value = "/GetReclamo/{numero}")
	public ResponseEntity<?> getReclamo(@PathVariable Integer numero){
		Optional<Reclamo> oReclamo = reclamoService.findById(numero);
		if(oReclamo.isPresent()) {
			Reclamo reclamo = oReclamo.get();
			registroService.save(new Registro("Solicito el reclamo " + numero,reclamo.getUsuario().getUsuario(),LocalDate.now()));
			List<Reclamo> reclamos = new ArrayList<>();
			reclamos.add(reclamo);
			return ResponseEntity.status(HttpStatus.OK).body(reclamos);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping(value = "/GetReclamosByPersona/{documento}")
	public ResponseEntity<?> getReclamosByPersona(@PathVariable String documento,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos,TipoPermiso.habitante);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		if(estado.getUsuario().tieneSoloEstePermiso(TipoPermiso.habitante)) {
			if(!estado.getUsuario().getPersona().getDocumento().equals(documento)) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sos la persona buscada");
			}
		}
		Optional<Persona> oPersona = personaService.findById(documento);
		if(oPersona.isPresent()) {
			Persona persona = oPersona.get();
			List<Reclamo> reclamos = reclamoService.findByPersona(persona);
			registroService.save(new Registro("Solicito los reclamos del habitante con documento " + documento,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(reclamos);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping(value = "/PostAgregarReclamoUnidad")
	public ResponseEntity<?> postAgregarReclamoUnidad(@RequestParam("codigo") Integer codigo,@RequestParam("piso") String piso,
			@RequestParam("numero") String numero, @RequestParam("documento") String documento,@RequestParam("ubicacion") String ubicacion,@RequestParam("descripcion") String descripcion,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos,TipoPermiso.habitante);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigo);
		Optional<Persona> oPersona = personaService.findById(documento);
		if(oPersona.isPresent() && oEdificio.isPresent()) {
			Persona persona = oPersona.get();
			Edificio edificio = oEdificio.get();
			List<Unidad> unidades = unidadService.findByEdificioAndPisoAndNumero(edificio, piso, numero);
			if(unidades.size()>0) {
				List<Reclamo> reclamos = new ArrayList<>();
				for (Unidad u: unidades) {
					if(estado.getUsuario().tieneSoloEstePermiso(TipoPermiso.habitante)) {
						if(u.getInquilinos().size()>0) {
							if(!u.tenesInquilino(estado.getUsuario().getPersona())) {
								return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sos Inquilino de esta unidad");
							}
						}
						else {
							if(!u.tenesDuenio(estado.getUsuario().getPersona())) {
								return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sos Duenio de esta unidad");
							}
						}
					}
					Reclamo reclamo = new Reclamo(persona, edificio, ubicacion, descripcion, u);
					reclamoService.save(reclamo);
					reclamos.add(reclamo);
				}
				registroService.save(new Registro("Agrego un reclamo a la unidad " + numero,estado.getUsuario(),LocalDate.now()));
				return ResponseEntity.status(HttpStatus.OK).body(reclamos);
			}else {
				return ResponseEntity.notFound().build();
			}
		}
		return ResponseEntity.notFound().build();
	}
	@CrossOrigin
	@PostMapping(value = "/PostAgregarReclamoUnidadById")
	public ResponseEntity<?> postAgregarReclamoUnidad(@RequestParam("id") Integer id, @RequestParam("documento") String documento,@RequestParam("ubicacion") String ubicacion,@RequestParam("descripcion") String descripcion,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos,TipoPermiso.habitante);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Persona> oPersona = personaService.findById(documento);
		if(oPersona.isPresent()) {
			Persona persona = oPersona.get();
			Unidad unidad = unidadService.findById(id).get();
			if(estado.getUsuario().tieneSoloEstePermiso(TipoPermiso.habitante)) {
				if(unidad.getInquilinos().size()>0) {
					if(!unidad.tenesInquilino(estado.getUsuario().getPersona())) {
						System.err.println("aca 1");
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sos Inquilino de esta unidad");
					}
				}
				else {
					if(!unidad.tenesDuenio(estado.getUsuario().getPersona())) {
						System.err.println("aca 2");
						return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sos Duenio de esta unidad");
					}
				}
			}
			Reclamo reclamo = new Reclamo(persona, unidad.getEdificio(), ubicacion, descripcion, unidad);
			reclamoService.save(reclamo);
			registroService.save(new Registro("Agrego un reclamo a la unidad " + id,estado.getUsuario(),LocalDate.now()));
			System.err.println("aca 3");
			return ResponseEntity.status(HttpStatus.OK).body(reclamo);
		}
		return ResponseEntity.notFound().build();
	}
	
	
	@PostMapping(value = "/PostAgregarReclamoEdificio")
	public ResponseEntity<?> postAgregarReclamoEdificio(@RequestParam("codigo") Integer codigo, @RequestParam("documento") String documento,
			@RequestParam("ubicacion") String ubicacion,@RequestParam("descripcion") String descripcion,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos,TipoPermiso.habitante);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Optional<Edificio> oEdificio = edificioService.findById(codigo);
		Optional<Persona> oPersona = personaService.findById(documento);
		if(oPersona.isPresent() && oEdificio.isPresent()) {
			Persona persona = oPersona.get();
			Edificio edificio = oEdificio.get();
			if(estado.getUsuario().tieneSoloEstePermiso(TipoPermiso.habitante)) {
				if(!edificio.tenesPersona(estado.getUsuario().getPersona())) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sos habitante de este edificio");
				}
			}
			Reclamo reclamo = new Reclamo(persona, edificio, ubicacion, descripcion);
			System.out.print("aca2");
			registroService.save(new Registro("Agrego un reclamo al edificio " + codigo,estado.getUsuario(),LocalDate.now()));
			System.out.print("aca");
			return ResponseEntity.status(HttpStatus.OK).body(reclamoService.save(reclamo));
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping(value ="/PutCambiarEstado/{numero}/{estado}")
	public ResponseEntity<?> putCambiarEstado(@PathVariable Integer numero,@PathVariable String estado,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estadoAcceso = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos);
		if (!estadoAcceso.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estadoAcceso.getMensaje());
		}
		Optional<Reclamo> oReclamo = reclamoService.findById(numero);
		if(oReclamo.isPresent()) {
			Reclamo reclamo = oReclamo.get();
			reclamo.cambiarEstado(enumConvert.convertToEntityAttribute(estado));
			registroService.save(new Registro("Cambio el estado del reclamo "+numero+" a "+estado,estadoAcceso.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(reclamoService.save(reclamo));
		}
		return ResponseEntity.notFound().build();
	}
	
	//---------------------
	//Imagen
	/*
	@PostMapping(value = "/PostAgregarImagen")
	public ResponseEntity<?> postAgregarImagen(@RequestParam("numero") Integer numero,@RequestParam("direccion") String direccion,
			@RequestParam("tipo") String tipo){
		Optional<Reclamo> oReclamo = reclamoService.findById(numero);
		if(oReclamo.isPresent()) {
			Reclamo reclamo = oReclamo.get();
			reclamo.agregarImagen(direccion, tipo);
			return ResponseEntity.status(HttpStatus.OK).body(reclamoService.save(reclamo));
		}
		return ResponseEntity.notFound().build();
	}*/
	
	@PostMapping("/PostAgregarImagen/upload")
	public ResponseEntity<?> upload (@RequestParam("File") MultipartFile imagen, @RequestParam("numero") Integer numero,
			@RequestParam("tipo") String tipo,@RequestParam("usuarioEmpleado") String usuarioEmpleado,@RequestParam("contraseñaEmpleado") String contraseñaEmpleado){
		Validacion estado = usuarioTieneAcceso(usuarioEmpleado,contraseñaEmpleado,TipoPermiso.administrador,TipoPermiso.gestionReclamos,TipoPermiso.habitante);
		if (!estado.getMensaje().equals("Ok")){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(estado.getMensaje());
		}
		Map<String, Object> response = new HashMap<>();
				
		Optional<Reclamo> oReclamo= reclamoService.findById(numero);
		
		if(!imagen.isEmpty() && oReclamo.isPresent()) {
			
			Reclamo reclamo = oReclamo.get();
			if(!reclamo.getUsuario().equals(estado.getUsuario().getPersona())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No sos el autor del reclamo");
			}
			
			String nombreImagen=""+ imagen.getOriginalFilename();
			java.nio.file.Path rutaFoto= Paths.get("uploads").resolve(nombreImagen).toAbsolutePath();
			String direccion=""+rutaFoto;
			try {
				Files.copy(imagen.getInputStream(),rutaFoto);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen" + nombreImagen);
				response.put("Error", e.getMessage().concat(":").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}

			registroService.save(new Registro("Grabo una imagen en el reclamo" + numero,estado.getUsuario(),LocalDate.now()));
			return ResponseEntity.status(HttpStatus.OK).body(imagenService.save(new Imagen(direccion,tipo,reclamo)));
			
		}
		return ResponseEntity.notFound().build();
	}
}
