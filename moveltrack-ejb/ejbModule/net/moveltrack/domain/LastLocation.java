package net.moveltrack.domain;

import net.moveltrack.domain.Location;

public class LastLocation{
	private Location location;
	private String placa;
	private Integer veiculoId;
	private String endereco;
	private String veiculoTipo;
	private String marcaModelo;
	private double distancia;
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public String getVeiculoTipo() {
		return veiculoTipo;
	}
	public void setVeiculoTipo(String veiculoTipo) {
		this.veiculoTipo = veiculoTipo;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public double getDistancia() {
		return distancia;
	}
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	public Integer getVeiculoId() {
		return veiculoId;
	}
	public void setVeiculoId(Integer veiculoId) {
		this.veiculoId = veiculoId;
	}
	
}