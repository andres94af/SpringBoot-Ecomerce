package com.curso.ecomerce.service;

import java.util.Optional;

import com.curso.ecomerce.model.Usuario;

public interface IUsuarioService {
	Optional<Usuario> findById(Integer id);
	Usuario save(Usuario usuario);
	Optional<Usuario> findByEmail(String email);
}
