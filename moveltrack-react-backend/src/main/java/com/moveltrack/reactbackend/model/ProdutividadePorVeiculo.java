package com.moveltrack.reactbackend.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ProdutividadePorVeiculo{
	
	@Id
	private Integer id;
	String placa;
	int qtdViagens;
	int qtdCidades;
	int qtdClientes;
	double valorDaCarga;
	int pesoDaCarga;
	double distanciaPercorrida;
	private double litros;
	private double kml;
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public int getQtdViagens() {
		return qtdViagens;
	}
	public void setQtdViagens(int qtdViagens) {
		this.qtdViagens = qtdViagens;
	}
	public int getQtdCidades() {
		return qtdCidades;
	}
	public void setQtdCidades(int qtdCidades) {
		this.qtdCidades = qtdCidades;
	}
	public int getQtdClientes() {
		return qtdClientes;
	}
	public void setQtdClientes(int qtdClientes) {
		this.qtdClientes = qtdClientes;
	}
	public double getValorDaCarga() {
		return valorDaCarga;
	}
	public void setValorDaCarga(double valorDaCarga) {
		this.valorDaCarga = valorDaCarga;
	}
	public int getPesoDaCarga() {
		return pesoDaCarga;
	}
	public void setPesoDaCarga(int pesoDaCarga) {
		this.pesoDaCarga = pesoDaCarga;
	}
	public double getDistanciaPercorrida() {
		return distanciaPercorrida;
	}
	public void setDistanciaPercorrida(double distanciaPercorrida) {
		this.distanciaPercorrida = distanciaPercorrida;
	}
	public double getLitros() {
		return litros;
	}
	public void setLitros(double litros) {
		this.litros = litros;
	}
	public double getKml() {
		return kml;
	}
	public void setKml(double kml) {
		this.kml = kml;
	}
	
	
	
}	