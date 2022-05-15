package com.omar.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.omar.springboot.backend.apirest.models.entity.Paciente;

public interface IPacienteDao extends CrudRepository<Paciente, Integer>{

}
