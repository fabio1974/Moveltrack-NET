package com.moveltrack.reactbackend.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class DespesaFrota  {
	
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


    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDaDespesa;


    @Size(max = 30)
    private String descricao;
    
    @ManyToOne
    private Veiculo veiculo;
    
    @ManyToOne
    private Motorista motorista;
    
    @ManyToOne
    private Cliente cliente;
    
    @ManyToOne
    private Viagem viagem;
    
    @Enumerated(EnumType.STRING)
    private DespesaFrotaCategoria categoria;

    @Enumerated(EnumType.STRING)
    private DespesaFrotaEspecie especie;
    
    @NotNull
    private double valor;
    
    private double litros;
    
    
    public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	@Temporal(TemporalType.TIMESTAMP)
    private Date dataDePagamento;

	public Date getDataDaDespesa() {
		return dataDaDespesa;
	}
	public void setDataDaDespesa(Date dataDaDespesa) {
		this.dataDaDespesa = dataDaDespesa;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public double getLitros() {
		return litros;
	}
	public void setLitros(double litros) {
		this.litros = litros;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
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
	public Viagem getViagem() {
		return viagem;
	}
	public void setViagem(Viagem viagem) {
		this.viagem = viagem;
	}
	public DespesaFrotaCategoria getCategoria() {
		return categoria;
	}
	public void setCategoria(DespesaFrotaCategoria categoria) {
		this.categoria = categoria;
	}
	public DespesaFrotaEspecie getEspecie() {
		return especie;
	}
	public void setEspecie(DespesaFrotaEspecie especie) {
		this.especie = especie;
	}
	public Date getDataDePagamento() {
		return dataDePagamento;
	}
	public void setDataDePagamento(Date dataDePagamento) {
		this.dataDePagamento = dataDePagamento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
}
