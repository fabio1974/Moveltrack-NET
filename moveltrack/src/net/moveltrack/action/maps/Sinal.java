package net.moveltrack.action.maps;

import java.io.Serializable;

import javax.faces.view.ViewScoped;

@ViewScoped
public class Sinal implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -425537111751097276L;
	
	private String bateria;
	private String voltagem;
	private String gsm;
	private String gps;
	private String satelites;
	private String bloqueio;
	private String ignicao;
	private String alarme;
	
	private String velocidade;
	private String distancia;
	
	public String getBateria() {
		return bateria;
	}
	public void setBateria(String bateria) {
		this.bateria = bateria;
	}
	public String getVoltagem() {
		return voltagem;
	}
	public void setVoltagem(String voltagem) {
		this.voltagem = voltagem;
	}
	public String getGsm() {
		return gsm;
	}
	public void setGsm(String gsm) {
		this.gsm = gsm;
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
	public String getSatelites() {
		return satelites;
	}
	public void setSatelites(String satelites) {
		this.satelites = satelites;
	}
	
	public String getBloqueio() {
		return bloqueio;
	}
	public void setBloqueio(String bloqueio) {
		this.bloqueio = bloqueio;
	}
	public String getIgnicao() {
		return ignicao;
	}
	public void setIgnicao(String ignicao) {
		this.ignicao = ignicao;
	}
	public String getAlarme() {
		return alarme;
	}
	public void setAlarme(String alarme) {
		this.alarme = alarme;
	}
	

	public String getVelocidade() {
		return velocidade;
	}
	public void setVelocidade(String velocidade) {
		this.velocidade = velocidade;
	}
	public String getDistancia() {
		return distancia;
	}
	public void setDistancia(String distancia) {
		this.distancia = distancia;
	}
	
	
	
	
}
