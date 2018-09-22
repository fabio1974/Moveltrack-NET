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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Despesa {
	
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

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDaDespesa;

    @NotNull
    @Size(max = 150)
    private String descricao;
    
    @Enumerated(EnumType.STRING)
    private DespesaTipo tipoDeDespesa;

    @NotNull
    private double valor;
    
    private Integer osid;    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDePagamento;


    @Enumerated(EnumType.STRING)
    private DespesaStatus status;

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

	
	public DespesaTipo getTipoDeDespesa() {
		return tipoDeDespesa;
	}
	public void setTipoDeDespesa(DespesaTipo tipoDeDespesa) {
		this.tipoDeDespesa = tipoDeDespesa;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}

	public Integer getOsid() {
		return osid;
	}
	public void setOsid(Integer osid) {
		this.osid = osid;
	}
	public Date getDataDePagamento() {
		return dataDePagamento;
	}
	public void setDataDePagamento(Date dataDePagamento) {
		this.dataDePagamento = dataDePagamento;
	}
	public DespesaStatus getStatus() {
		return status;
	}
	public void setStatus(DespesaStatus status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
