package com.moveltrack.reactbackend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RelatorioVeiculo{
	
	@Id
	private Integer id;
	private String situacao;
	private String endereco;
	private Date chegada;
	private Date saida;
	private String permanencia;
    private double latitude;
    private double longitude;
    private double velocidade;

    public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSituacao() {
		return situacao;
	}
	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public Date getChegada() {
		return chegada;
	}
	public void setChegada(Date chegada) {
		this.chegada = chegada;
	}
	public Date getSaida() {
		return saida;
	}
	public void setSaida(Date saida) {
		this.saida = saida;
	}
	public String getPermanencia() {
		return permanencia;
	}
	public void setPermanencia(String permanencia) {
		this.permanencia = permanencia;
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
	
	
	
}	