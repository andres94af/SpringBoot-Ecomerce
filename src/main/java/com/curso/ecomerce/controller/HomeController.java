package com.curso.ecomerce.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.curso.ecomerce.model.Usuario;
import com.curso.ecomerce.service.IDetalleOrdenService;
import com.curso.ecomerce.service.IOrdenService;
import com.curso.ecomerce.service.IUsuarioService;
import com.curso.ecomerce.service.ProductoService;
import com.itextpdf.text.DocumentException;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProductoService productoService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private IOrdenService ordenService;

	@Autowired
	private IDetalleOrdenService detalleOrdenService;

	// almacena los detalles de la orden
	List<DetalleOrden> detalles = new ArrayList<DetalleOrden>();

	// datos de la orden
	Orden orden = new Orden();

	@GetMapping("")
	public String home(Model model, HttpSession session) {
		List<Producto> productos = productoService.findAll();
		LOGGER.info("Sesion del usuario: {}", session.getAttribute("idusuario"));
		model.addAttribute("productos", productos);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/home";
	}

	@GetMapping("/productohome/{id}")
	public String productoHome(Model model, @PathVariable Integer id, HttpSession session) {
		Optional<Producto> producto = productoService.get(id);
		Producto p = producto.get();
		LOGGER.info("Este es el producto recibido {}", p);
		model.addAttribute("producto", p);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/productohome";
	}

	@PostMapping("/cart")
	public String addCart(@RequestParam Integer id, @RequestParam Integer cantidad, Model model, HttpSession session) {
		if (session.getAttribute("idusuario") != null) {
			DetalleOrden detalleOrden = new DetalleOrden();
			Producto producto = new Producto();
			Optional<Producto> optionalProducto = productoService.get(id);
			producto = optionalProducto.get();
			detalleOrden.setCantidad(cantidad);
			detalleOrden.setNombre(producto.getNombre());
			detalleOrden.setPrecio(producto.getPrecio());
			detalleOrden.setTotal(producto.getPrecio() * cantidad);
			detalleOrden.setProducto(producto);
			// validar que el producto no se añada mas de una vez al carrito
			Integer idProducto = producto.getId();
			boolean ingresado = detalles.stream().anyMatch(p -> p.getProducto().getId() == idProducto);
			if (!ingresado) {
				detalles.add(detalleOrden);
			}else {
				model.addAttribute("sesion", session.getAttribute("idusuario"));
				model.addAttribute("nombreProducto", producto.getNombre());
				return "usuario/producto_agregado";
			}
			double sumaTotal = 0;
			sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
			orden.setTotal(sumaTotal);
			model.addAttribute("cart", detalles);
			model.addAttribute("orden", orden);
			model.addAttribute("sesion", session.getAttribute("idusuario"));
			return "usuario/carrito";
		}else {
			return "usuario/login";
		}
	}

	// elimina un producto del carrito
	@GetMapping("/delete/cart/{id}")
	public String deleteProductoCart(@PathVariable Integer id, Model model, HttpSession session) {
		List<DetalleOrden> ordenesNueva = new ArrayList<DetalleOrden>();
		for (DetalleOrden detalleOrden : detalles) {
			if (detalleOrden.getProducto().getId() != id) {
				ordenesNueva.add(detalleOrden);
			}
		}
		detalles = ordenesNueva;
		double sumaTotal = 0;
		sumaTotal = detalles.stream().mapToDouble(dt -> dt.getTotal()).sum();
		orden.setTotal(sumaTotal);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/carrito";
	}

	@GetMapping("/getCart")
	public String getCart(Model model, HttpSession session) {
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/carrito";
	}

	@GetMapping("/order")
	public String order(Model model, HttpSession session) {
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		model.addAttribute("usuario", usuario);
		model.addAttribute("cart", detalles);
		model.addAttribute("orden", orden);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/resumenorden";
	}

	@GetMapping("/saveOrder")
	public String saveOrder(HttpSession session) {
		// obtiene fecha y usuario para asignar a la orden
		Date fechaCreacion = new Date();
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		// asigna fecha, numero, usuario y guarda la orden en bbdd
		orden.setFechaCreacion(fechaCreacion);
		orden.setNumero(ordenService.generarNumeroOrden());
		orden.setUsuario(usuario);
		ordenService.save(orden);
		// a cada detalle le asigna la orden, resta productos comprados y guarda en bbdd
		for (DetalleOrden dt : detalles) {
			Producto producto = dt.getProducto();
			int cantidad = dt.getCantidad();
			producto.setCantidad(producto.getCantidad()-cantidad);
			productoService.update(producto);
			dt.setOrden(orden);
			detalleOrdenService.save(dt);
		}
		//crea pdf de la orden
		try {
			ordenService.generarOrdenPDF(orden, detalles);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		// limpiar detalles y orden (carrito). Luego redirecciona a la home
		orden = new Orden();
		detalles.clear();
		return "redirect:/";
	}

	@PostMapping("/search")
	public String searchProduct(@RequestParam String nombre, Model model, HttpSession session) {
		String nombreMinuscula = nombre.toLowerCase();
		LOGGER.info("nombre del producto a buscar: {}", nombreMinuscula);
		List<Producto> productos = productoService.findAll().stream()
				.filter(p -> p.getNombre().toLowerCase().contains(nombreMinuscula)).collect(Collectors.toList());
		model.addAttribute("productos", productos);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		return "usuario/home";
	}

}
