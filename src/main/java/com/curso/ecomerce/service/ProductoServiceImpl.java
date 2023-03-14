package com.curso.ecomerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecomerce.model.Producto;
import com.curso.ecomerce.repository.ProductoRespository;

@Service
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoRespository repository;

	@Override
	public Producto save(Producto producto) {
		return repository.save(producto);
	}

	@Override
	public Optional<Producto> get(Integer id) {
		return repository.findById(id);
	}

	@Override
	public void update(Producto producto) {
		repository.save(producto);
	}

	@Override
	public void delete(Integer id) {
		repository.deleteById(id);
	}

	@Override
	public List<Producto> findAll() {
		return repository.findAll();
	}

}
