package com.curso.ecomerce.service;

import java.util.List;

import com.curso.ecomerce.model.Orden;

public interface IOrdenService {
	List<Orden> findAll();
	Orden save (Orden orden);
}
