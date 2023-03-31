package com.curso.ecomerce.controller;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.curso.ecomerce.model.Orden;
import com.curso.ecomerce.model.Usuario;
import com.curso.ecomerce.service.IOrdenService;
import com.curso.ecomerce.service.IUsuarioService;
import com.curso.ecomerce.service.MailService;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IOrdenService ordenService;
	
	@Autowired
	private MailService mailService;
	
	BCryptPasswordEncoder passEncode = new BCryptPasswordEncoder();

	@GetMapping("/registro")
	public String create() {
		return "usuario/registro";
	}

	@PostMapping("/save")
	public String saveUser(Usuario usuario) {
		usuario.setTipo("USER");
		usuario.setPassword(passEncode.encode(usuario.getPassword()));
		try {
			usuarioService.save(usuario);			
		} catch (Exception e) {
			e.printStackTrace();
			return "usuario/registro_fallido";
		}
		//enviar mail usuario
		
		mailService.enviarMailRegistro(usuario);
		
		//establecer usuario 1 como admin
		Usuario usuarioAdm = usuarioService.findById(1).get();
		usuarioAdm.setTipo("ADMIN");
		usuarioService.save(usuarioAdm);
		return "usuario/registro_exitoso";
	}
	
	@PostMapping("/update")
	public String updateUser(Usuario usuario) {
		usuario.setTipo("USER");
		usuario.setPassword(passEncode.encode(usuario.getPassword()));
		usuarioService.update(usuario);
		LOGGER.info("Actualizado correctamente el usuario: {}", usuario);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login() {
		return "usuario/login";
	}

	@GetMapping("/acceder")
	public String acceder(Usuario usuario, HttpSession session) {
		Optional<Usuario> user = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString()));
		if (user.isPresent()) {
			session.setAttribute("idusuario", user.get().getId());
			if (user.get().getTipo().equals("ADMIN")) {
				return "redirect:/administrador";
			} else {
				return "redirect:/";
			}
		} else {
			LOGGER.info("Usuario no existe");
		}
		return "redirect:/";
	}
	
	@GetMapping("/compras")
	public String obtenerCompras(HttpSession session, Model model) {
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		List<Orden> ordenes = ordenService.findByUsuario(usuario);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		model.addAttribute("ordenes", ordenes);
		return "usuario/compras";
	}
	
	@GetMapping("/detalle/{id}")
	public String detalleCompra(@PathVariable Integer id, HttpSession session, Model model) {
		LOGGER.info("id de la orden: {}", id);
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		Orden orden = ordenService.findById(id).get();
		model.addAttribute("detalles", orden.getDetalle());
		return "usuario/detallecompra";
	}
	
	@GetMapping("/datospersonales")
	public String datosPersonales(Model model, HttpSession session) {
		Usuario usuario = usuarioService.findById(Integer.parseInt(session.getAttribute("idusuario").toString())).get();
		model.addAttribute("sesion", session.getAttribute("idusuario"));
		model.addAttribute("usuario", usuario);
		return "usuario/datos_personales";
	}
	
	@GetMapping("cerrar")
	public String cerrarSesion(HttpSession session) {
		session.removeAttribute("idusuario");
		return "redirect:/";
	}

}
