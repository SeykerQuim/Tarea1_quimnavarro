package com.quimnv.modelo;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Espectaculo implements Serializable{
	
    private Long idEspectaculo;
    private String nombre;
    private LocalDate fechaini;
    private LocalDate fechafin;
    private Long idCoord;
    private Set<Numero> numeros = new HashSet<>();
    
    public Espectaculo() {}
    
    

	public Espectaculo(Long idEspectaculo, String nombre, LocalDate fechaini, LocalDate fechafin, Long idCoord) {
		super();
		this.idEspectaculo = idEspectaculo;
		this.nombre = nombre;
		this.fechaini = fechaini;
		this.fechafin = fechafin;
		this.idCoord = idCoord;
	}



	public Espectaculo(Long idEspectaculo, String nombre, LocalDate fechai, LocalDate fechafin, Long idCoord,
			Set<Numero> numeros) {
		super();
		this.idEspectaculo = idEspectaculo;
		this.nombre = nombre;
		this.fechaini = fechai;
		this.fechafin = fechafin;
		this.idCoord = idCoord;
		this.numeros = numeros;
	}

	public Long getIdEspectaculo() {
		return idEspectaculo;
	}

	public void setIdEspectaculo(Long idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaini() {
		return fechaini;
	}

	public void setFechaini(LocalDate fechaini) {
		this.fechaini = fechaini;
	}

	public LocalDate getFechafin() {
		return fechafin;
	}

	public void setFechafin(LocalDate fechafin) {
		this.fechafin = fechafin;
	}

	public Long getIdCoord() {
		return idCoord;
	}

	public void setIdCoord(Long idCoord) {
		this.idCoord = idCoord;
	}

	public Set<Numero> getNumeros() {
		return numeros;
	}

	public void setNumeros(Set<Numero> numeros) {
		this.numeros = numeros;
	}
    
	
}
