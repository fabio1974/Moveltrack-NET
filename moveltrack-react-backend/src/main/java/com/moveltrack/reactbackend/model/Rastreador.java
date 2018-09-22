package com.moveltrack.reactbackend.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

@Entity
public class Rastreador {
	
	private static final long serialVersionUID = -5543915465870023729L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition="serial")
	private Integer id;
 
	
    @OneToOne
    private Chip chip;

    
   
    @Enumerated(EnumType.STRING)
    private RastreadorTipo rastreadorTipo;

    

    @Size(max = 8, message = "O tamanho da senha deve ser no máximo de 8 caracteres!")
    private String senha;

    private String imei;
    
    private boolean ativoBotaoPanico;

    private boolean ativoEscuta;

    private boolean ativoCercaVirtual;
    
    private boolean ativoBloqueio;

    @Size(max = 150)
    private String observacao;
    

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Chip getChip() {
		return chip;
	}


	public void setChip(Chip chip) {
		this.chip = chip;
	}




	public boolean isAtivoBotaoPanico() {
		return ativoBotaoPanico;
	}


	public void setAtivoBotaoPanico(boolean ativoBotaoPanico) {
		this.ativoBotaoPanico = ativoBotaoPanico;
	}


	public boolean isAtivoEscuta() {
		return ativoEscuta;
	}


	public void setAtivoEscuta(boolean ativoEscuta) {
		this.ativoEscuta = ativoEscuta;
	}


	public boolean isAtivoCercaVirtual() {
		return ativoCercaVirtual;
	}


	public void setAtivoCercaVirtual(boolean ativoCercaVirtual) {
		this.ativoCercaVirtual = ativoCercaVirtual;
	}


	public boolean isAtivoBloqueio() {
		return ativoBloqueio;
	}


	public void setAtivoBloqueio(boolean ativoBloqueio) {
		this.ativoBloqueio = ativoBloqueio;
	}





	public RastreadorTipo getRastreadorTipo() {
		return rastreadorTipo;
	}


	public void setRastreadorTipo(RastreadorTipo rastreadorTipo) {
		this.rastreadorTipo = rastreadorTipo;
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



	public String getObservacao() {
		return observacao;
	}


	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}




	
    
}
