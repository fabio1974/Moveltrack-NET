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



@Entity
public class Contrato extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 249187095475644915L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	private String numeroContrato;
	

	
    @ManyToOne
    private Cliente cliente;


    @Temporal(TemporalType.TIMESTAMP)
    private Date inicio;
    

    @Temporal(TemporalType.TIMESTAMP)
    private Date fim;
    

    private int diaVencimento;

    @ManyToOne
    private Pessoa vendedor;
    
    private boolean pagamentoAnual;
    
    @Enumerated(EnumType.STRING)
    private ContratoGeraCarne contratoGeraCarne;

    @Enumerated(EnumType.STRING)
    protected ContratoTipo contratoTipo;

    private double mensalidade;
    
    private double entrada;
    
    @Enumerated(EnumType.STRING)
    private ContratoStatus status;
    

    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
    
    @ManyToOne
    private Pessoa alterador;
   

	public String getNumeroContrato() {
		return numeroContrato;
	}
	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFim() {
		return fim;
	}
	public void setFim(Date fim) {
		this.fim = fim;
	}
	public int getDiaVencimento() {
		return diaVencimento;
	}
	public void setDiaVencimento(int diaVencimento) {
		this.diaVencimento = diaVencimento;
	}
	public Pessoa getVendedor() {
		return vendedor;
	}
	public void setVendedor(Pessoa vendedor) {
		this.vendedor = vendedor;
	}
	public boolean isPagamentoAnual() {
		return pagamentoAnual;
	}
	public void setPagamentoAnual(boolean pagamentoAnual) {
		this.pagamentoAnual = pagamentoAnual;
	}
	public ContratoTipo getContratoTipo() {
		return contratoTipo;
	}
	public void setContratoTipo(ContratoTipo contratoTipo) {
		this.contratoTipo = contratoTipo;
	}

	public double getMensalidade() {
		return mensalidade;
	}
	public void setMensalidade(double mensalidade) {
		this.mensalidade = mensalidade;
	}
	public double getEntrada() {
		return entrada;
	}
	public void setEntrada(double entrada) {
		this.entrada = entrada;
	}
	public ContratoStatus getStatus() {
		return status;
	}
	public void setStatus(ContratoStatus status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Date getUltimaAlteracao() {
		return ultimaAlteracao;
	}
	public void setUltimaAlteracao(Date ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}
	public Pessoa getAlterador() {
		return alterador;
	}
	public void setAlterador(Pessoa alterador) {
		this.alterador = alterador;
	}
	public ContratoGeraCarne getContratoGeraCarne() {
		return contratoGeraCarne;
	}
	public void setContratoGeraCarne(ContratoGeraCarne contratoGeraCarne) {
		this.contratoGeraCarne = contratoGeraCarne;
	}
    
    
}
