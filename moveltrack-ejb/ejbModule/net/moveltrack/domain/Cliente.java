package net.moveltrack.domain;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Cliente extends Pessoa {


	private static final long serialVersionUID = -8204391964448267728L;
	
 /*   @NotNull
    @Enumerated(EnumType.STRING)
    private PerfilTipo tipo;*/

/*	@NotNull
    private boolean isPessoaJuridica;*/
    
    @Size(max = 40)
    private String nomeFantasia;

    @Size(max = 100)
    private String observacao;
    
    private String emailAlarme;
    
    @ManyToOne
    private Cerca cerca;
 
    @Transient
    private Contrato contrato;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaCobranca;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimoLembrete;


/*	public PerfilTipo getTipo() {
		return tipo;
	}

	public void setTipo(PerfilTipo tipo) {
		this.tipo = tipo;
	}*/

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}
	
	public Cerca getCerca() {
		return cerca;
	}

	public void setCerca(Cerca cerca) {
		this.cerca = cerca;
	}

	public String getEmailAlarme() {
		return emailAlarme;
	}

	public void setEmailAlarme(String emailAlarme) {
		this.emailAlarme = emailAlarme;
	}

/*	public boolean isPessoaJuridica() {
		return isPessoaJuridica;
	}

	public void setPessoaJuridica(boolean isPessoaJuridica) {
		this.isPessoaJuridica = isPessoaJuridica;
	}*/

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	public boolean isPessoaJuridica() {
		return this.getUsuario().getPerfil().getTipo() == PerfilTipo.CLIENTE_PJ;
	}	

	
	public Date getUltimaCobranca() {
		return ultimaCobranca;
	}

	public void setUltimaCobranca(Date ultimaCobranca) {
		this.ultimaCobranca = ultimaCobranca;
	}
	
	

	public Date getUltimoLembrete() {
		return ultimoLembrete;
	}

	public void setUltimoLembrete(Date ultimoLembrete) {
		this.ultimoLembrete = ultimoLembrete;
	}

	@Override
	public String toString(){
		return getUsuario().getPerfil().getTipo()==PerfilTipo.CLIENTE_PJ?nomeFantasia:getNome();
	}
}