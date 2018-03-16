package net.moveltrack.domain;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Equipamento extends BaseEntity {
	
	private static final long serialVersionUID = -5543915465870023729L;

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
    private Date dataCadastro;

 
    @Enumerated(EnumType.STRING)
    private EquipamentoSituacao situacao;

    @ManyToOne
    private Cliente proprietario;

 
    
    @Enumerated(EnumType.STRING)
    private ModeloRastreador modelo;


    @Size(max = 6)
    private String senha;


    private String imei;
    
    private int atrasoGmt;

    @OneToOne
    private Chip chip;

    private int comando;

    @Size(max = 150)
    private String observacao;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
    
    @ManyToOne
    private Pessoa alterador;

    @Override
    public String toString() {

        return imei + ((chip != null) ? (" - CEL: " + chip.getNumero()) : "") + " - " + modelo.toString();
    }

    /**
     */
    @ManyToOne
    private Empregado possuidor;

    
    
    

	public int getAtrasoGmt() {
		return atrasoGmt;
	}
	public void setAtrasoGmt(int atrasoGmt) {
		this.atrasoGmt = atrasoGmt;
	}
	public Date getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	public EquipamentoSituacao getSituacao() {
		return situacao;
	}
	public void setSituacao(EquipamentoSituacao situacao) {
		this.situacao = situacao;
	}
	public Cliente getProprietario() {
		return proprietario;
	}
	public void setProprietario(Cliente proprietario) {
		this.proprietario = proprietario;
	}
	public ModeloRastreador getModelo() {
		return modelo;
	}
	public void setModelo(ModeloRastreador modelo) {
		this.modelo = modelo;
	}

	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}

	public Chip getChip() {
		return chip;
	}
	public void setChip(Chip chip) {
		this.chip = chip;
	}
	public int getComando() {
		return comando;
	}
	public void setComando(int comando) {
		this.comando = comando;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public Empregado getPossuidor() {
		return possuidor;
	}
	public void setPossuidor(Empregado possuidor) {
		this.possuidor = possuidor;
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

    
}
