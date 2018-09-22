package com.moveltrack.reactbackend.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ConsumoPorVeiculo{
	
	@Id
	private Integer id;
	String placa;
	String marcaModelo;
	int qtdViagens;
	double distanciaPercorrida;
	double distanciaHodometro;
	private double litros;
	private double kml;
	private double kml2;
	private double erro;
	
	
	
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
	public double getDistanciaHodometro() {
		return distanciaHodometro;
	}
	public void setDistanciaHodometro(double distanciaHodometro) {
		this.distanciaHodometro = distanciaHodometro;
	}
	public double getKml2() {
		return kml2;
	}
	public void setKml2(double kml2) {
		this.kml2 = kml2;
	}
	public double getErro() {
		return erro;
	}
	public void setErro(double erro) {
		this.erro = erro;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	
	
	
	
	
}	