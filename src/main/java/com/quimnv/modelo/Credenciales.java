package com.quimnv.modelo;

public class Credenciales {

	protected long id;
	protected String nombre;
	protected String contrassenya;
	protected Perfiles perfil;
	
	public Credenciales() {
	}

	public Credenciales(long id, String nombre, String contrassenya, Perfiles perfil) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.contrassenya = contrassenya;
		this.perfil = perfil;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getContrassenya() {
		return contrassenya;
	}

	public void setContrassenya(String contrassenya) {
		this.contrassenya = contrassenya;
	}

	public Perfiles getPerfil() {
		return perfil;
	}

	public void setPerfil(Perfiles perfil) {
		this.perfil = perfil;
	}
	
	
}
