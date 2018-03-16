package net.moveltrack.domain;

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
public class Lancamento extends BaseEntity {
	
	private static final long serialVersionUID = -3033918338861753241L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Override
	public Integer getId() {
		return id;
	}
	@Override
	public void setId(Integer id) {
		this.id = id;
	}
 
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;
    private double valor;
    
    @Enumerated(EnumType.STRING)
    private LancamentoTipo operacao;

    @Enumerated(EnumType.STRING)
    private LancamentoStatus status;
    
    @Enumerated(EnumType.STRING)
    private HouveBaixa houveBaixa;
    
    @Enumerated(EnumType.STRING)
    private LancamentoFormaPagamento formaPagamento;

    @Size(max = 300)
    private String observacao;
    
    @ManyToOne
    private OrdemDeServico ordemDeServico;

    @ManyToOne
    private Empregado solicitante;

    @ManyToOne
    private Empregado operador;

	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public LancamentoTipo getOperacao() {
		return operacao;
	}
	public void setOperacao(LancamentoTipo operacao) {
		this.operacao = operacao;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public OrdemDeServico getOrdemDeServico() {
		return ordemDeServico;
	}
	public void setOrdemDeServico(OrdemDeServico ordemDeServico) {
		this.ordemDeServico = ordemDeServico;
	}
	public Empregado getSolicitante() {
		return solicitante;
	}
	public void setSolicitante(Empregado solicitante) {
		this.solicitante = solicitante;
	}
	public Empregado getOperador() {
		return operador;
	}
	public void setOperador(Empregado operador) {
		this.operador = operador;
	}
	public LancamentoStatus getStatus() {
		return status;
	}
	public void setStatus(LancamentoStatus status) {
		this.status = status;
	}
	public LancamentoFormaPagamento getFormaPagamento() {
		return formaPagamento;
	}
	public void setFormaPagamento(LancamentoFormaPagamento formaPagamento) {
		this.formaPagamento = formaPagamento;
	}
	public HouveBaixa getHouveBaixa() {
		return houveBaixa;
	}
	public void setHouveBaixa(HouveBaixa houveBaixa) {
		this.houveBaixa = houveBaixa;
	}
    
}
