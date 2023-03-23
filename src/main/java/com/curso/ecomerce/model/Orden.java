package com.curso.ecomerce.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "ordenes")
public class Orden {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String numero;
	private Date fechaCreacion;
	private double total;
	@ManyToOne
	private Usuario usuario;
	@OneToMany(mappedBy = "orden")
	private List<DetalleOrden> detalle;
	
	public Orden() {
		// TODO Auto-generated constructor stub
	}

	public Orden(Integer id, String numero, Date fechaCreacion, double total) {
		this.id = id;
		this.numero = numero;
		this.fechaCreacion = fechaCreacion;
		this.total = total;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<DetalleOrden> getDetalle() {
		return detalle;
	}

	public void setDetalle(List<DetalleOrden> detalle) {
		this.detalle = detalle;
	}

	@Override
	public String toString() {
		return "Orden [id=" + id + ", numero=" + numero + ", fechaCreacion=" + fechaCreacion + ", total=" + total + "]";
	}
	
	

}
