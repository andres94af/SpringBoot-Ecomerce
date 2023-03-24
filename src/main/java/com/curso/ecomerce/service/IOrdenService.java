package com.curso.ecomerce.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import com.curso.ecomerce.model.DetalleOrden;
import com.curso.ecomerce.model.Orden;
import com.curso.ecomerce.model.Usuario;
import com.itextpdf.text.DocumentException;

public interface IOrdenService {
	List<Orden> findAll();
	Orden save (Orden orden);
	String generarNumeroOrden();
	List<Orden> findByUsuario(Usuario usuario);
	Optional<Orden> findById(Integer id);
	void generarOrdenPDF(Orden orden, List<DetalleOrden> detalles)throws DocumentException, FileNotFoundException;
}
