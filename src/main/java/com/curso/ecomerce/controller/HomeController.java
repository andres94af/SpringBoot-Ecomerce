package com.curso.ecomerce.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.curso.ecomerce.model.DetalleOrden;
import com.curso.ecomerce.model.Orden;
import com.curso.ecomerce.model.Producto;
import com.curso.ecomerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProductoService productoService;
	
	//almacena los detalles de la ordem
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();
	
	//datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model) {
		List<Producto> productos = productoService.findAll();
		model.addAttribute("productos", productos);
		return "usuario/home";
	}
	
	@GetMapping("/productohome/{id}")
	public String productoHome(Model model,@PathVariable Integer id) {
		Optional<Producto> producto = productoService.get(id);
		Producto p = producto.get();
		LOGGER.info("Este es el producto recibido {}", p);
		model.addAttribute("producto", p);
		return "usuario/productohome";
	}
	
	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad) {
		DetalleOrden detalleOrden = new DetalleOrden();
		Producto producto = new Producto();
		double subTotal = 0;
		
		Optional<Producto> optionalProducto = productoService.get(id);
		LOGGER.info("producto añadido: {}", optionalProducto.get());
		LOGGER.info("cantidad del producto añadida: {}", cantidad);
		
		return "usuario/carrito";
	}

}
