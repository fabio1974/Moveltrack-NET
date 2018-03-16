package net.moveltrack.domain;

import java.util.Date;


public class RelatorioMensalDespesa{
	
	private int ano;
	private int mes;
	private Date data;
	private double carga;  
	private double combustivel;        
	private double manutencao;     
	private double estivas; 
	private double diarias; 
	private double ipva; 
	private double transito; 
	private double trabalhistas; 
	private double outras; 
	private double despesa;
	

	
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
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
	public double getCarga() {
		return carga;
	}
	public void setCarga(double carga) {
		this.carga = carga;
	}
	public double getCombustivel() {
		return combustivel;
	}
	public void setCombustivel(double combustivel) {
		this.combustivel = combustivel;
	}
	public double getManutencao() {
		return manutencao;
	}
	public void setManutencao(double manutencao) {
		this.manutencao = manutencao;
	}
	public double getEstivas() {
		return estivas;
	}
	public void setEstivas(double estivas) {
		this.estivas = estivas;
	}
	public double getDiarias() {
		return diarias;
	}
	public void setDiarias(double diarias) {
		this.diarias = diarias;
	}
	public double getIpva() {
		return ipva;
	}
	public void setIpva(double ipva) {
		this.ipva = ipva;
	}
	public double getTransito() {
		return transito;
	}
	public void setTransito(double transito) {
		this.transito = transito;
	}
	public double getTrabalhistas() {
		return trabalhistas;
	}
	public void setTrabalhistas(double trabalhistas) {
		this.trabalhistas = trabalhistas;
	}
	public double getOutras() {
		return outras;
	}
	public void setOutras(double outras) {
		this.outras = outras;
	}
	public double getDespesa() {
		return despesa;
	}
	public void setDespesa(double despesa) {
		this.despesa = despesa;
	}  
}	