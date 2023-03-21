package com.curso.ecomerce.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.curso.ecomerce.model.Orden;
import com.curso.ecomerce.model.Usuario;
import com.curso.ecomerce.repository.IOrdenRepository;

@Service
public class OrdenServiceImpl implements IOrdenService {
	
	@Autowired
	private IOrdenRepository ordenRepository;

	@Override
	public Orden save(Orden orden) {
		return ordenRepository.save(orden);
	}

	@Override
	public List<Orden> findAll() {
		return ordenRepository.findAll();
	}
	
	@Override
	public String generarNumeroOrden() {
		int numero = 0;
		String numeroConcatenado = "";
		//crea lista de todas las ordenes
		List<Orden> ordenes = findAll();
		//Crea una lista para almacenar los numeros como Integer porque de la tabla vienen como string
		List<Integer> numeros = new ArrayList<Integer>();
		//por cada una de las ordenes parsea de string a integer y lo agrega a la lista de numeros
		ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
		//Condicional que obtiene el numero de orden mas grande, lo asigna a la variable numero y le suma 1
		if (ordenes.isEmpty()) {
			numero = 1;
		}else {
			numero = numeros.stream().max(Integer::compare).get();
			numero++;
		}
		//agrega CEROS al cmienzo del numero de orden
		if (numero<10) {
			numeroConcatenado = "0000"+String.valueOf(numero);
		}else if(numero<100) {
			numeroConcatenado = "000"+String.valueOf(numero);
		}else if(numero<1000) {
			numeroConcatenado = "00"+String.valueOf(numero);
		}else if(numero<10000) {
			numeroConcatenado = "0"+String.valueOf(numero);
		}
		return numeroConcatenado;
	}

	@Override
	public List<Orden> findByUsuario(Usuario usuario) {
		return ordenRepository.findByUsuario(usuario);
	}

	@Override
	public Optional<Orden> findById(Integer id) {
		return ordenRepository.findById(id);
	}

}
