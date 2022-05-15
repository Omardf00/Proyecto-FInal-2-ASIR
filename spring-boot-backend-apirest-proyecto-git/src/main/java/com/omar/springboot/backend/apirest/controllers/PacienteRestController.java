package com.omar.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.omar.springboot.backend.apirest.models.entity.Paciente;
import com.omar.springboot.backend.apirest.models.service.IPacienteService;

@CrossOrigin(origins = {"http://localhost:4200"})  //Permite la conexión al puerto de la aplicación con Angular
@RestController
@RequestMapping("/api")
public class PacienteRestController {
	
	@Autowired
	private IPacienteService pacienteService;
	
	@GetMapping("/pacientes")
	public List<Paciente> index(){
		return pacienteService.findAll();
	}
	
	@GetMapping("/pacientes/{id}")
	public ResponseEntity<?> show(@PathVariable Integer id) {
		
		Paciente paciente = null;
		Map<String, Object> response = new HashMap<>();
		
		try {
			
			paciente = pacienteService.findById(id);
			
		} catch(DataAccessException e) {
			
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("mensaje", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		
		if(paciente == null) {
			response.put("mensaje", "El paciente con el ID: ".concat(id.toString().concat(" no existe en la base de datos")));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Paciente>(paciente, HttpStatus.OK);
	}
	
	@PostMapping("/pacientes")
	public ResponseEntity<?> create(@Valid @RequestBody Paciente paciente, BindingResult result) {
		
		Paciente pacienteNew = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			
			
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		try {
			
			pacienteNew = pacienteService.save(paciente);
			
		} catch(DataAccessException e) {
			
			response.put("mensaje", "Error al realizar la insercion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		response.put("mensaje","El paciente ha sido creado con éxito");
		response.put("paciente", pacienteNew);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/pacientes/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Paciente paciente, BindingResult result, @PathVariable Integer id) {
		Paciente pacienteActual = pacienteService.findById(id);
		Paciente pacienteUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
			if(result.hasErrors()) {
				
				List<String> errors = result.getFieldErrors()
						.stream()
						.map(err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
						.collect(Collectors.toList());
				
				
				response.put("errors", errors);
				return new ResponseEntity<Map<String, Object>>(response,HttpStatus.BAD_REQUEST);
			}
		
		
		if(pacienteActual == null) {
			response.put("mensaje", "El paciente con el ID: ".concat(id.toString().concat(" no existe en la base de datos, por lo que no podemos actualizarlo")));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.NOT_FOUND);
		}
		try {
		pacienteActual.setNombre(paciente.getNombre());
		pacienteActual.setApellido1(paciente.getApellido1());
		pacienteActual.setApellido2(paciente.getApellido2());
		pacienteActual.setTelefono(paciente.getTelefono());
		pacienteActual.setEdad(paciente.getEdad());
		pacienteActual.setSexo(paciente.getSexo());
		
		pacienteUpdated = pacienteService.save(pacienteActual);
		} catch(DataAccessException e) {
			
			response.put("mensaje", "Error al actualizar en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		response.put("mensaje","El paciente ha sido actualizado con éxito");
		response.put("paciente", pacienteUpdated);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);	
	}
	
	@DeleteMapping("/pacientes/{id}")
	public ResponseEntity<?> delete(@PathVariable Integer id) {
		
		Map<String, Object> response = new HashMap<>();
		
		try {
		pacienteService.delete(id);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al eliminar el paciente en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		
		response.put("mensaje", "El paciente ha sido eliminado con éxito");
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		
	}

}
