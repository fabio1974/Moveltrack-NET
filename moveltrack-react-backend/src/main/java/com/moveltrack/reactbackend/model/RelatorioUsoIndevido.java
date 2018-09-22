package com.moveltrack.reactbackend.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class RelatorioUsoIndevido {
	
	private static final long serialVersionUID = 6939092515889595427L;

	@Id
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


	private int ano;
	private int mes;
	private int dia;
	private int hora;
	private String placa;
	private String marcaModelo;
	private String nomeMotorista;
	private String diaSemana;
	private Date data;
	
	

	public String getDiaSemana() {
		return diaSemana;
	}
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getHora() {
		return hora;
	}
	public void setHora(int hora) {
		this.hora = hora;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getNomeMotorista() {
		return nomeMotorista;
	}
	public void setNomeMotorista(String nomeMotorista) {
		this.nomeMotorista = nomeMotorista;
	}
	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	
	
	
    
}
