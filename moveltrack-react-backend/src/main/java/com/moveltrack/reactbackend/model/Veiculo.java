package com.moveltrack.reactbackend.model;
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


@Entity
public class Veiculo {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	protected String placa;

    @NotNull
    private String marcaModelo;
    
    private String motorista;


    private String descricao;
    
    private double velocidadeMaxima;
    

    @NotNull
    @Enumerated(EnumType.STRING)
    private Cor cor;


    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInstalacao;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDesinstalacao;
    
    
    @OneToOne
    private Equipamento equipamento;


    @ManyToOne
    private Empregado instalador;


    @ManyToOne
    private Contrato contrato;

    private boolean botaoPanico;

    private boolean demo;

    private boolean comEscuta;

    private boolean comCercaVirtual;
    
    private boolean comBloqueio;
    
    private double consumoCombustivel;
    

    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
    
    @ManyToOne
    private Pessoa alterador;
    

    @NotNull
    @Enumerated(EnumType.STRING)
    private VeiculoTipo tipo;

    
    @Enumerated(EnumType.STRING)
    private VeiculoStatus status;
    
    public boolean isComBloqueio() {
		return comBloqueio;
	}
    
	public void setComBloqueio(boolean comBloqueio) {
		this.comBloqueio = comBloqueio;
	}
	
	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getMarcaModelo() {
		return marcaModelo;
	}

	public void setMarcaModelo(String marcaModelo) {
		this.marcaModelo = marcaModelo;
	}

	public double getConsumoCombustivel() {
		return consumoCombustivel;
	}
	public void setConsumoCombustivel(double consumoCombustivel) {
		this.consumoCombustivel = consumoCombustivel;
	}
	public Cor getCor() {
		return cor;
	}




	public void setCor(Cor cor) {
		this.cor = cor;
	}


	public Date getDataInstalacao() {
		return dataInstalacao;
	}


	public Date getDataDesinstalacao() {
		return dataDesinstalacao;
	}

	public void setDataDesinstalacao(Date dataDesinstalacao) {
		this.dataDesinstalacao = dataDesinstalacao;
	}
	
	public void setDataInstalacao(Date dataInstalacao) {
		this.dataInstalacao = dataInstalacao;
	}

	public Equipamento getEquipamento() {
		return equipamento;
	}




	public void setEquipamento(Equipamento equipamento) {
		this.equipamento = equipamento;
	}




	public Empregado getInstalador() {
		return instalador;
	}




	public void setInstalador(Empregado instalador) {
		this.instalador = instalador;
	}




	public Contrato getContrato() {
		return contrato;
	}




	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}




	public boolean isBotaoPanico() {
		return botaoPanico;
	}




	public void setBotaoPanico(boolean botaoPanico) {
		this.botaoPanico = botaoPanico;
	}




	public boolean isDemo() {
		return demo;
	}




	public void setDemo(boolean demo) {
		this.demo = demo;
	}




	public boolean isComEscuta() {
		return comEscuta;
	}




	public void setComEscuta(boolean comEscuta) {
		this.comEscuta = comEscuta;
	}




	public boolean isComCercaVirtual() {
		return comCercaVirtual;
	}




	public void setComCercaVirtual(boolean comCercaVirtual) {
		this.comCercaVirtual = comCercaVirtual;
	}




	public VeiculoTipo getTipo() {
		return tipo;
	}




	public void setTipo(VeiculoTipo tipo) {
		this.tipo = tipo;
	}




	public VeiculoStatus getStatus() {
		return status;
	}


	public double getVelocidadeMaxima() {
		return velocidadeMaxima;
	}
	public void setVelocidadeMaxima(double velocidadeMaxima) {
		this.velocidadeMaxima = velocidadeMaxima;
	}
	public void setStatus(VeiculoStatus status) {
		this.status = status;
	}

	public String getMotorista() {
		return motorista;
	}
	public void setMotorista(String motorista) {
		this.motorista = motorista;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
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
        return ((placa == null || placa.trim().equals("")) ? "SEM PLACA" : placa) + " - " + marcaModelo + " - " + cor;
    }

}
