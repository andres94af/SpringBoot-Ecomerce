package com.curso.ecomerce.service;

import java.util.Optional;

import com.curso.ecomerce.model.Producto;

public interface ProductoService {
	public Producto save(Producto producto);
	public Optional<Producto> get(Integer id);
	public void update(Producto producto);
	public void delete(Integer id);
}
