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
public class Chip extends BaseEntity {
	
	private static final long serialVersionUID = -1633936031016219418L;

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
    @Size(max = 30)
    private String numero;
    
    @Size(max = 30)
    private String iccid;
    
	
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;
    

    @NotNull
    @Enumerated(EnumType.STRING)
    private Operadora operadora;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
    
    @ManyToOne
    private Pessoa alterador;    
    
    @Enumerated(EnumType.STRING)
    private ChipStatus status = ChipStatus.ATIVO;  
    
 	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}


	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Operadora getOperadora() {
		return operadora;
	}

	public void setOperadora(Operadora operadora) {
		this.operadora = operadora;
	}
	
	

	public ChipStatus getStatus() {
		return status;
	}
	public void setStatus(ChipStatus status) {
		this.status = status;
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
	@Override
    public String toString() {
        return numero + " - " + iccid+ " - "+operadora;
    }
}