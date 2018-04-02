package net.moveltrack.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RelatorioExcessoVelocidade{
	
	@Id
	private Integer id;
	private String endereco;
	private Date dateLocation;
	private String placa;
	private String marcaModelo;
    private double latitude;
    private double longitude;
    private double velocidade;
    private double distancia;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Date getDateLocation() {
		return dateLocation;
	}
	public void setDateLocation(Date dateLocation) {
		this.dateLocation = dateLocation;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
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
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getVelocidade() {
		return velocidade;
	}
	public void setVelocidade(double velocidade) {
		this.velocidade = velocidade;
	}
	public double getDistancia() {
		return distancia;
	}
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	
}	