package com.quimnv.modelo;

public class Persona {
	protected Long id;
	protected String email;
	protected String nombre;
	protected String nacionalidad;
	
	protected Credenciales credenciales;
	
	public Persona() {}

	public Persona(Long id, String email, String nombre, String nacionalidad) {
		super();
		this.id = id;
		this.email = email;
		this.nombre = nombre;
		this.nacionalidad = nacionalidad;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}
	
	
	
}
