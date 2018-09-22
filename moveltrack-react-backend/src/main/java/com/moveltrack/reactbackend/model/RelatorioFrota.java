package com.moveltrack.reactbackend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RelatorioFrota{
	
	@Id
	private Integer id;
	private String motorista;
	private int ano;
	private int mes;
	private Date data;
	private String destino;
	private String estado;
	private int qtdViagens;
	private double diasViagens;
	private int qtdCidades;
	private int qtdClientes;
	private double valorDaCarga;
	private int pesoDaCarga;
	private double despesaEstiva;
	private double despesaDiaria;
	private double despesaCombustivel;
	private double despesaTotal;
	private double distanciaPercorrida;
	private double litros;
	private double kml;
	private double valorDevolucao;
	public int getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getMotorista() {
		return motorista;
	}
	public void setMotorista(String motorista) {
		this.motorista = motorista;
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
	public String getDestino() {
		return destino;
	}
	
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public int getQtdViagens() {
		return qtdViagens;
	}
	public void setQtdViagens(int qtdViagens) {
		this.qtdViagens = qtdViagens;
	}
	public double getDiasViagens() {
		return diasViagens;
	}
	public void setDiasViagens(double diasViagens) {
		this.diasViagens = diasViagens;
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
	public double getDespesaEstiva() {
		return despesaEstiva;
	}
	public void setDespesaEstiva(double despesaEstiva) {
		this.despesaEstiva = despesaEstiva;
	}
	public double getDespesaCombustivel() {
		return despesaCombustivel;
	}
	public void setDespesaCombustivel(double despesaCombustivel) {
		this.despesaCombustivel = despesaCombustivel;
	}
	public double getDespesaTotal() {
		return despesaTotal;
	}
	public void setDespesaTotal(double despesaTotal) {
		this.despesaTotal = despesaTotal;
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
	public double getValorDevolucao() {
		return valorDevolucao;
	}
	public void setValorDevolucao(double valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
	}
	public double getDespesaDiaria() {
		return despesaDiaria;
	}
	public void setDespesaDiaria(double despesaDiaria) {
		this.despesaDiaria = despesaDiaria;
	}
	

	
	
	
	
	

}	