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
import com.curso.ecomerce.service.IUsuarioService;
import com.curso.ecomerce.service.ProductoService;
import com.curso.ecomerce.service.UploadFileService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/productos")
public class ProductoController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private IUsuarioService usuarioservice;
	
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
	public String save(Producto producto,@RequestParam("img") MultipartFile file, HttpSession session) throws IOException {
		LOGGER.info("Este es el objeto producto {}", producto);
		Usuario usuario = usuarioservice.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		producto.setUsuario(usuario);
		// Imagen
		if (producto.getId()==null) {//cuando crea producto y el id es nulo
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}else {
			
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
	public String update(Producto producto, @RequestParam("img") MultipartFile file) throws IOException {
		Producto p = new Producto();
		p = productoService.get(producto.getId()).get();
		if (file.isEmpty()) {//cuando se modifica el producto pero mantiene la imagen
			producto.setImagen(p.getImagen());
		}else {//cuando se modifica el producto y se modifica tambien la imagen
			
			if (!p.getImagen().equals("default.jpg")) {//elimina la imagen del servidor cuando no es la img por defecto
				upload.deleteImage(p.getImagen());
			}
			String nombreImagen = upload.saveImage(file);
			producto.setImagen(nombreImagen);
		}
		producto.setUsuario(p.getUsuario());
		productoService.update(producto);
		return "redirect:/productos";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) {
		Producto p = new Producto();
		p=productoService.get(id).get();
		if (!p.getImagen().equals("default.jpg")) {//elimina la imagen del servidor cuando no es la img por defecto
			upload.deleteImage(p.getImagen());
		}
		productoService.delete(id);
		return "redirect:/productos";
	}
	
}
