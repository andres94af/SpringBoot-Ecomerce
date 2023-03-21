package com.curso.ecomerce.service;

import java.util.List;
import java.util.Optional;

import com.curso.ecomerce.model.Orden;
import com.curso.ecomerce.model.Usuario;

public interface IOrdenService {
	List<Orden> findAll();
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);
	Optional<Orden> findById(Integer id);
}
