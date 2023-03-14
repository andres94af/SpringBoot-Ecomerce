package com.curso.ecomerce.controller;

import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.curso.ecomerce.model.Producto;
import com.curso.ecomerce.model.Usuario;
import com.curso.ecomerce.service.ProductoService;
import com.curso.ecomerce.service.UploadFileService;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UploadFileService upload;
	
	@GetMapping("")
	public String show(Model model) {
		model.addAttribute("productos", productoService.findAll());
		return "productos/show";
	}
	
	@GetMapping("/create")
	public String create() {
		return "productos/create";
	}
	
	@PostMapping("/save")
	public String save(Producto producto,@RequestParam("img") MultipartFile file) throws IOException {
		LOGGER.info("Este es el objeto producto {}", producto);
		Usuario usuario = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(usuario);
		// Imagen
		if (producto.getId()==null) {//cuando crea producto y el id es nulo
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			if (file.isEmpty()) {//cuando se modifica el producto pero mantiene la imagen
				Producto p = new Producto();
				p = productoService.get(producto.getId()).get();
				producto.setImagen(p.getImagen());
			}else {//cuando se modifica el producto y se modifica tambien la imagen
				String nombreImagen = upload.saveImage(file);
				producto.setImagen(nombreImagen);
			}
		}
		
		productoService.save(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/edit/{id}")
	public String edit(@PathVariable Integer id, Model model) {
		Producto producto = new Producto();
		Optional<Producto> optionalProducto = productoService.get(id);
		producto = optionalProducto.get();
		LOGGER.info("Producto a editar {}", producto);
		model.addAttribute("producto", producto);
		return "productos/edit";
	}
	
	@PostMapping("/update")
	public String update(Producto producto) {
		Usuario usuario = new Usuario(1, "", "", "", "", "", "", "");
		producto.setUsuario(usuario);
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		productoService.delete(id);
		return "redirect:/productos";
	}
	
}
