package com.omar.springboot.backend.apirest.models.service;

import java.util.List;

import com.omar.springboot.backend.apirest.models.entity.Paciente;

public interface IPacienteService {
	
	public List<Paciente> findAll();
	
	public Paciente findById(Integer id);
	
	public Paciente save(Paciente paciente);
	
	public void delete(Integer id);

}
