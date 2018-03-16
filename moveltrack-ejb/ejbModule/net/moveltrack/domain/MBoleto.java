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

import org.apache.commons.lang.StringUtils;



@Entity
public class MBoleto extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3033918338861753241L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Override
	public Integer getId() {
		// TODO Auto-generated method stub
		return id;
	}
	@Override
	public void setId(Integer id) {
		this.id = id;
		
	}
	

 
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataVencimento;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataPagamento;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataRegistroPagamento;

    @NotNull
    private double valor;
    
    @NotNull
    private double multa;
    
    @NotNull
    private double juros;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private MBoletoStatus situacao;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoDeCobranca tipoDeCobranca;

    
    @NotNull
    @Enumerated(EnumType.STRING)
    private MBoletoTipo tipo;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEmissao;

    @Size(max = 80)
    private String mensagem34;
    

    @Size(max = 300)
    private String observacao;
    
    @NotNull
    private String nossoNumero;

    @ManyToOne
    private Remessa remessa;
    
    @ManyToOne
    private Carne carne;

    @NotNull
    @ManyToOne
    private Contrato contrato;
    
    @ManyToOne
    private Empregado emissor;
    
    @ManyToOne
	private Iugu iugu;

	public TipoDeCobranca getTipoDeCobranca() {
		return tipoDeCobranca;
	}
	public void setTipoDeCobranca(TipoDeCobranca tipoDeCobranca) {
		this.tipoDeCobranca = tipoDeCobranca;
	}
	public Date getDataVencimento() {
		return dataVencimento;
	}
	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}
	public Date getDataPagamento() {
		return dataPagamento;
	}
	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	public Date getDataRegistroPagamento() {
		return dataRegistroPagamento;
	}
	public void setDataRegistroPagamento(Date dataRegistroPagamento) {
		this.dataRegistroPagamento = dataRegistroPagamento;
	}

	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public MBoletoStatus getSituacao() {
		return situacao;
	}
	public void setSituacao(MBoletoStatus situacao) {
		this.situacao = situacao;
	}
	public MBoletoTipo getTipo() {
		return tipo;
	}
	public void setTipo(MBoletoTipo tipo) {
		this.tipo = tipo;
	}
	public Date getDataEmissao() {
		return dataEmissao;
	}
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public String getMensagem34() {
		return mensagem34;
	}
	public void setMensagem34(String mensagem34) {
		this.mensagem34 = mensagem34;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Remessa getRemessa() {
		return remessa;
	}
	public void setRemessa(Remessa remessa) {
		this.remessa = remessa;
	}
	public Empregado getEmissor() {
		return emissor;
	}
	public void setEmissor(Empregado emissor) {
		this.emissor = emissor;
	}
	public Contrato getContrato() {
		return contrato;
	}
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	public String getNossoNumero() {
		return nossoNumero;
	}
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}
	public double getMulta() {
		return multa;
	}
	public void setMulta(double multa) {
		this.multa = multa;
	}
	public Carne getCarne() {
		return carne;
	}
	public void setCarne(Carne carne) {
		this.carne = carne;
	}
	public double getJuros() {
		return juros;
	}
	public void setJuros(double juros) {
		this.juros = juros;
	}
	public Iugu getIugu() {
		return iugu;
	}
	public void setIugu(Iugu iugu) {
		this.iugu = iugu;
	}

    
}
