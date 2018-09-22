package com.moveltrack.reactbackend.model;
import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class RelatorioSinalRegistro implements Serializable {

	
	@Id
	private String imei;
    private String senha;

    @Enumerated(EnumType.STRING)
    private ModeloRastreador modelo;
	
    private String numero;

    @Enumerated(EnumType.STRING)
    private Operadora operadora;
    
    @Enumerated(EnumType.STRING)
    private EquipamentoSituacao equipamentoSituacao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLocation;
    
    @Enumerated(EnumType.STRING)
    private ContratoStatus contratoStatus;
    
    private String placa;
    
    private String marcaModelo;
    
    @Enumerated(EnumType.STRING)
    private VeiculoTipo tipo;
    
    private String nome;
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public ModeloRastreador getModelo() {
		return modelo;
	}
	public void setModelo(ModeloRastreador modelo) {
		this.modelo = modelo;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Operadora getOperadora() {
		return operadora;
	}
	public void setOperadora(Operadora operadora) {
		this.operadora = operadora;
	}
	public EquipamentoSituacao getEquipamentoSituacao() {
		return equipamentoSituacao;
	}
	public void setEquipamentoSituacao(EquipamentoSituacao equipamentoSituacao) {
		this.equipamentoSituacao = equipamentoSituacao;
	}
	public Date getDateLocation() {
		return dateLocation;
	}
	public void setDateLocation(Date dateLocation) {
		this.dateLocation = dateLocation;
	}
	public ContratoStatus getContratoStatus() {
		return contratoStatus;
	}
	public void setContratoStatus(ContratoStatus contratoStatus) {
		this.contratoStatus = contratoStatus;
	}
	public String getPlaca() {
		return placa;
	}
	public void setPlaca(String placa) {
		this.placa = placa;
	}
	public VeiculoTipo getTipo() {
		return tipo;
	}
	public void setTipo(VeiculoTipo tipo) {
		this.tipo = tipo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getMarcaModelo() {
		return marcaModelo;
	}
	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}
	
}
