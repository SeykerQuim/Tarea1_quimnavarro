package com.quimnv.modelo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Artista extends Persona {

	private Long idArt;
	private String apodo = null;
	private Set<Especialidad> especialidades = new HashSet<>();
	
	private List<Numero> numeros = new ArrayList<>();
	
	public Artista() {}

	public Artista(Long idArt, String apodo, Set<Especialidad> especialidades, List<Numero> numeros) {
		super();
		this.idArt = idArt;
		this.apodo = apodo;
		this.especialidades = especialidades;
		this.numeros = null;
	}
	
	public Artista(Long id, String email, String nombre, String nacionalidad, Long idArt, String apodo, Set<Especialidad> especialidades) {
        super(id, email, nombre, nacionalidad);
        this.idArt = id;
        this.apodo = apodo;
        this.especialidades = especialidades;
    }




	public Long getIdArt() {
		return idArt;
	}

	public void setIdArt(Long idArt) {
		this.idArt = idArt;
	}

	public String getApodo() {
		return apodo;
	}

	public void setApodo(String apodo) {
		this.apodo = apodo;
	}

	public Set<Especialidad> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(Set<Especialidad> especialidades) {
		this.especialidades = especialidades;
	}

	public List<Numero> getNumeros() {
		return numeros;
	}

	public void setNumeros(List<Numero> numeros) {
		this.numeros = numeros;
	}
	
	

	
}
