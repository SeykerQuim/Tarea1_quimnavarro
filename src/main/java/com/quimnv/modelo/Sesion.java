package com.quimnv.modelo;


public class Sesion {

	private String nombre;
	private Perfiles perfil;
	
	
	public Sesion(String nombre, Perfiles perfil) {
		super();
		this.nombre = nombre;
		this.perfil = perfil;
	}
	
	public Sesion() {}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Perfiles getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfiles perfil) {
		this.perfil = perfil;
	}
	
	
}
