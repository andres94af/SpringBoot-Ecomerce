package com.curso.ecomerce.controller;

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

import com.curso.ecomerce.model.Producto;
import com.curso.ecomerce.service.ProductoService;

@Controller
@RequestMapping("/")
public class HomeController {
	
	Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProductoService productoService;

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
	public String addCart() {
		return "usuario/carrito";
	}

}
