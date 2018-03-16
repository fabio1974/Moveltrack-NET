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
public class OrdemDeServico extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7053245849524003588L;

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
    private Date dataDoServico;
    
    @ManyToOne
    private Cliente cliente;
    
    @ManyToOne
    private Veiculo veiculo;

    @Size(max = 150)
    private String observacao;

    @Size(max = 150)
    private String numero;

    @ManyToOne
    private Empregado operador;

    @Enumerated(EnumType.STRING)
    private OrdemDeServicoTipo servico;

    private double valorDoServico;
    
    @Enumerated(EnumType.STRING)
    private OrdemDeServicoStatus status;
    

    
    
    public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Date getDataDoServico() {
		return dataDoServico;
	}
	public void setDataDoServico(Date dataDoServico) {
		this.dataDoServico = dataDoServico;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Empregado getOperador() {
		return operador;
	}
	public void setOperador(Empregado operador) {
		this.operador = operador;
	}
	public OrdemDeServicoTipo getServico() {
		return servico;
	}
	public void setServico(OrdemDeServicoTipo servico) {
		this.servico = servico;
	}
	public double getValorDoServico() {
		return valorDoServico;
	}
	public void setValorDoServico(double valorDoServico) {
		this.valorDoServico = valorDoServico;
	}
	public OrdemDeServicoStatus getStatus() {
		return status;
	}
	public void setStatus(OrdemDeServicoStatus status) {
		this.status = status;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}

	
	
}
