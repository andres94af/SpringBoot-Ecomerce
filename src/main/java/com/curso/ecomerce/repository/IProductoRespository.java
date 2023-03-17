package com.curso.ecomerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.curso.ecomerce.model.Producto;

@Repository
public interface IProductoRespository extends JpaRepository<Producto, Integer> {

}
