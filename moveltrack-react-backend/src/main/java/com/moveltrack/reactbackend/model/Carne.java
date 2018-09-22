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




@Entity
public class Carne{
	
	private static final long serialVersionUID = 6939092515889595427L;

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
    @ManyToOne
    private Contrato contrato;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoDeCobranca tipoDeCobranca;

	@ManyToOne
	private CarneConteudo conteudo;
	
	private int quantidadeBoleto;
	
	//private double mensalidade;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimentoInicio;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimentoFim;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataReferencia;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postagem;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmissao;

	public Contrato getContrato() {
		return contrato;
	}
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	public CarneConteudo getConteudo() {
		return conteudo;
	}
	public void setConteudo(CarneConteudo conteudo) {
		this.conteudo = conteudo;
	}
	public int getQuantidadeBoleto() {
		return quantidadeBoleto;
	}
	public void setQuantidadeBoleto(int quantidadeBoleto) {
		this.quantidadeBoleto = quantidadeBoleto;
	}
	public Date getDataVencimentoInicio() {
		return dataVencimentoInicio;
	}
	public void setDataVencimentoInicio(Date dataVencimentoInicio) {
		this.dataVencimentoInicio = dataVencimentoInicio;
	}
	public Date getDataVencimentoFim() {
		return dataVencimentoFim;
	}
	public void setDataVencimentoFim(Date dataVencimentoFim) {
		this.dataVencimentoFim = dataVencimentoFim;
	}
	public Date getPostagem() {
		return postagem;
	}
	public void setPostagem(Date postagem) {
		this.postagem = postagem;
	}
	public Date getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
/*	public double getMensalidade() {
		return mensalidade;
	}
	public void setMensalidade(double mensalidade) {
		this.mensalidade = mensalidade;
	}*/
	public TipoDeCobranca getTipoDeCobranca() {
		return tipoDeCobranca;
	}
	public void setTipoDeCobranca(TipoDeCobranca tipoDeCobranca) {
		this.tipoDeCobranca = tipoDeCobranca;
	}
	public Date getDataReferencia() {
		return dataReferencia;
	}
	public void setDataReferencia(Date dataReferencia) {
		this.dataReferencia = dataReferencia;
	}


}
