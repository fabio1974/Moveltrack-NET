package com.moveltrack.cartaoprograma.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;




@Entity
public class Chip {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="serial")
	private Integer id;
	
    private String numero;
    
    @Size(max = 20)
    private String iccid;


    @Enumerated(EnumType.STRING)	
    private Operadora operadora;
    
    
    
    
 	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}



	public Operadora getOperadora() {
		return operadora;
	}


	public void setOperadora(Operadora operadora) {
		this.operadora = operadora;
	}


	
}