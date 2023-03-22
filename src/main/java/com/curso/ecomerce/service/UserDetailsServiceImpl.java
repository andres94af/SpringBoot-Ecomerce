package com.curso.ecomerce.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.curso.ecomerce.model.Usuario;

import javax.servlet.http.HttpSession;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	HttpSession session;

//	private BCryptPasswordEncoder bCrypt = new BCryptPasswordEncoder();

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LOGGER.info("Esto es el username: {}", username);
		Optional<Usuario> optionalUser = usuarioService.findByEmail(username);
		if(optionalUser.isPresent()) {
			LOGGER.info("Esto es el id del usuario: {}", optionalUser.get().getId());
			session.setAttribute("idusuario", optionalUser.get().getId());
			Usuario usuario = optionalUser.get();
			return User.builder().username(usuario.getNombre()).password(usuario.getPassword()).roles(usuario.getTipo()).build();
		}else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
	}

}
