package com.moveltrack.cartaoprograma.model;
// Generated 23/07/2018 04:54:41 by Hibernate Tools 4.3.5.Final

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Operacao implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="serial")
	private int id;

	private String nome;
	
	private String local;
	
	private String objetivo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date inicio;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date fim;
	
    @NotNull
    @Enumerated(EnumType.STRING)
	private OperacaoStatus status;

	
	@ManyToMany
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	
	@ManyToMany
	private List<Arma> armas = new ArrayList<Arma>(0);
	
	@ManyToMany
	private List<Viatura> viaturas = new ArrayList<Viatura>(0);
	
	@OneToMany
	private List<EquipamentoDeApoio> equipamentoDeApoios = new ArrayList<EquipamentoDeApoio>();
	
	private String narrativaFinal;
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="operacao")
	private List<Acidente> acidentes = new ArrayList<Acidente>();
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="operacao")
	private List<PessoaApreendida> pessoaApreendidas = new ArrayList<PessoaApreendida>();
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="operacao")
	private List<Crime> crimes = new ArrayList<Crime>();
	
	@OneToMany(cascade=CascadeType.ALL,mappedBy="operacao")
	private List<DrogaApreendida> drogaApreendidas = new ArrayList<DrogaApreendida>();
	

	@OneToMany(cascade=CascadeType.ALL,mappedBy="operacao")
	private List<ArmaApreendida> armaApreendidas = new ArrayList<ArmaApreendida>();
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getObjetivo() {
		return objetivo;
	}

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
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

	public OperacaoStatus getStatus() {
		return status;
	}

	public void setStatus(OperacaoStatus status) {
		this.status = status;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public List<Arma> getArmas() {
		return armas;
	}

	public void setArmas(List<Arma> armas) {
		this.armas = armas;
	}

	public List<Viatura> getViaturas() {
		return viaturas;
	}

	public void setViaturas(List<Viatura> viaturas) {
		this.viaturas = viaturas;
	}

	public List<EquipamentoDeApoio> getEquipamentoDeApoios() {
		return equipamentoDeApoios;
	}

	public void setEquipamentoDeApoios(List<EquipamentoDeApoio> equipamentoDeApoios) {
		this.equipamentoDeApoios = equipamentoDeApoios;
	}

	public String getNarrativaFinal() {
		return narrativaFinal;
	}

	public void setNarrativaFinal(String narrativaFinal) {
		this.narrativaFinal = narrativaFinal;
	}

	public List<Acidente> getAcidentes() {
		return acidentes;
	}

	public void setAcidentes(List<Acidente> acidentes) {
		this.acidentes = acidentes;
	}

	public List<PessoaApreendida> getPessoaApreendidas() {
		return pessoaApreendidas;
	}

	public void setPessoaApreendidas(List<PessoaApreendida> pessoaApreendidas) {
		this.pessoaApreendidas = pessoaApreendidas;
	}

	public List<Crime> getCrimes() {
		return crimes;
	}

	public void setCrimes(List<Crime> crimes) {
		this.crimes = crimes;
	}

	public List<DrogaApreendida> getDrogaApreendidas() {
		return drogaApreendidas;
	}

	public void setDrogaApreendidas(List<DrogaApreendida> drogaApreendidas) {
		this.drogaApreendidas = drogaApreendidas;
	}

	public List<ArmaApreendida> getArmaApreendidas() {
		return armaApreendidas;
	}

	public void setArmaApreendidas(List<ArmaApreendida> armaApreendidas) {
		this.armaApreendidas = armaApreendidas;
	}

	
	
	

	
}
