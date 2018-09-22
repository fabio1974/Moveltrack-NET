package com.moveltrack.reactbackend.model;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;


@Entity
public class Poligono{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1323004107026182545L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

    @OneToOne
    private Veiculo veiculo;
    
    @Transient
    private List<Vertice> vertices;
    
    @Transient
    private String placa;
    
    private double centroLat;
    private double centroLng;
    
	private int zoom;
    
    private boolean ativo;
    
    private boolean bloqueia;
    
    private boolean enviaEmail;
    

	public double getCentroLat() {
		return centroLat;
	}
	public void setCentroLat(double centroLat) {
		this.centroLat = centroLat;
	}
	public double getCentroLng() {
		return centroLng;
	}
	public void setCentroLng(double centroLng) {
		this.centroLng = centroLng;
	}
	public int getZoom() {
		return zoom;
	}
	public void setZoom(int zoom) {
		this.zoom = zoom;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	public boolean isBloqueia() {
		return bloqueia;
	}
	public void setBloqueia(boolean bloqueia) {
		this.bloqueia = bloqueia;
	}
	
	public boolean isEnviaEmail() {
		return enviaEmail;
	}
	public void setEnviaEmail(boolean enviaEmail) {
		this.enviaEmail = enviaEmail;
	}
	public List<Vertice> getVertices() {
		return vertices;
	}
	public void setVertices(List<Vertice> vertices) {
		this.vertices = vertices;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
}
