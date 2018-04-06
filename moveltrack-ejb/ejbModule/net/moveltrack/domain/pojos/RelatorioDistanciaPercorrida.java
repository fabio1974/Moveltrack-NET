package net.moveltrack.domain.pojos;

import java.io.Serializable;

public class RelatorioDistanciaPercorrida implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String placa;
	private String marcaModelo;
	private Double distancia;
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	public Double getDistancia() {
		return distancia;
	}
	public void setDistancia(Double distancia) {
		this.distancia = distancia;
	}
	
	

}
