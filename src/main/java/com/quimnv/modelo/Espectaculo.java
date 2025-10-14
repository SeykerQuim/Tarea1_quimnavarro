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
    
    
}
