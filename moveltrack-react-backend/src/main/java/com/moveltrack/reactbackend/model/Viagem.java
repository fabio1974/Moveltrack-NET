package com.moveltrack.reactbackend.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Viagem{
	
	private static final long serialVersionUID = -3378138988214642824L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	private Integer numeroViagem;

	
	private String descricao;
	
    @ManyToOne
	private Cliente cliente;
	
    @ManyToOne
	private Veiculo veiculo;
    
    @ManyToOne
	private Motorista motorista;
    
    @ManyToOne
	private Municipio cidadeOrigem;
    
    @ManyToOne
	private Municipio cidadeDestino;
    
    @Enumerated(EnumType.STRING)
    private ViagemStatus status;
    
    @Temporal(TemporalType.TIMESTAMP)
	private Date partida;
    
    @Temporal(TemporalType.TIMESTAMP)
	private Date saiuDaCerca;
    
    
    @Temporal(TemporalType.TIMESTAMP)
	private Date entrouNaCerca;

    @Temporal(TemporalType.TIMESTAMP)
	private Date chegadaReal;
    
    
    @Temporal(TemporalType.TIMESTAMP)
	private Date chegadaPrevista;

    private double valorDaCarga;

    private double valorDevolucao;
    
    private int pesoDaCarga;
    
    private double distanciaPercorrida;
    
    private double distanciaHodometro;
    
	private int qtdCidades;
	private int qtdClientes;

	
	public double getValorDevolucao() {
		return valorDevolucao;
	}

	public void setValorDevolucao(double valorDevolucao) {
		this.valorDevolucao = valorDevolucao;
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
		//System.out.println("incrementando distancia da viagem "+id+" de "+this.distanciaPercorrida+ " para "+distanciaPercorrida);
		this.distanciaPercorrida = distanciaPercorrida;
	}
	
	public double getDistanciaHodometro() {
		return distanciaHodometro;
	}
	public void setDistanciaHodometro(double distanciaHodometro) {
		this.distanciaHodometro = distanciaHodometro;
	}
	public Integer getNumeroViagem() {
		return numeroViagem;
	}
	public void setNumeroViagem(Integer numeroViagem) {
		this.numeroViagem = numeroViagem;
	}
	
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public ViagemStatus getStatus() {
		return status;
	}
	public void setStatus(ViagemStatus status) {
		System.out.println("alterando viagem "+id+" de "+this.status+ " para "+status);
		this.status = status;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	public Motorista getMotorista() {
		return motorista;
	}
	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}
	public Municipio getCidadeOrigem() {
		return cidadeOrigem;
	}
	public void setCidadeOrigem(Municipio cidadeOrigem) {
		this.cidadeOrigem = cidadeOrigem;
	}
	public Municipio getCidadeDestino() {
		return cidadeDestino;
	}
	public void setCidadeDestino(Municipio cidadeDestino) {
		this.cidadeDestino = cidadeDestino;
	}

	public Date getSaiuDaCerca() {
		return saiuDaCerca;
	}
	public void setSaiuDaCerca(Date saiuDaCerca) {
		System.out.println("saiuDaCerca da viagem "+id+" de "+this.saiuDaCerca+ " para "+saiuDaCerca);
		this.saiuDaCerca = saiuDaCerca;
	}
	public Date getEntrouNaCerca() {
		return entrouNaCerca;
	}
	public void setEntrouNaCerca(Date entrouNaCerca) {
		System.out.println("entrouNaCerca da viagem "+id+" de "+this.entrouNaCerca+ " para "+entrouNaCerca);
		this.entrouNaCerca = entrouNaCerca;
	}
	public Date getChegadaPrevista() {
		return chegadaPrevista;
	}
	public void setChegadaPrevista(Date chegadaPrevista) {
		this.chegadaPrevista = chegadaPrevista;
	}
	public double getValorDaCarga() {
		return valorDaCarga;
	}
	public void setValorDaCarga(double valorDaCarga) {
		this.valorDaCarga = valorDaCarga;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getPartida() {
		return partida;
	}
	public void setPartida(Date partida) {
		this.partida = partida;
	}
	public Date getChegadaReal() {
		return chegadaReal;
	}
	public void setChegadaReal(Date chegadaReal) {
		System.out.println("chegadaReal da viagem "+id+" de "+this.chegadaReal+ " para "+chegadaReal);
		this.chegadaReal = chegadaReal;
	}
	
	public String numeroViagemFormatado(){
		if(this.getNumeroViagem()==null)
			return null;
		return String.format("%05d",getNumeroViagem().intValue());
	}
    
}
