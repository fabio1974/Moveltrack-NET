package net.moveltrack.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.UniqueConstraint;


@Entity
public class RelatorioUsoIndevidoParam extends BaseEntity {
	
	private static final long serialVersionUID = -8229122252019772789L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	@ManyToOne
	Cliente cliente;
	
	float velocidade;
	
	Date segundaInicio;
	Date segundaFim;
	Date tercaInicio;
	Date tercaFim;
	Date quartaInicio;
	Date quartaFim;
	Date quintaInicio;
	Date quintaFim;
	Date sextaInicio;
	Date sextaFim;
	Date sabadoInicio;
	Date sabadoFim;
	Date domingoInicio;
	Date domingoFim;

	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public float getVelocidade() {
		return velocidade;
	}
	public void setVelocidade(float velocidade) {
		this.velocidade = velocidade;
	}
	public Date getSegundaInicio() {
		return segundaInicio;
	}
	public void setSegundaInicio(Date segundaInicio) {
		this.segundaInicio = segundaInicio;
	}
	public Date getSegundaFim() {
		return segundaFim;
	}
	public void setSegundaFim(Date segundaFim) {
		this.segundaFim = segundaFim;
	}
	public Date getTercaInicio() {
		return tercaInicio;
	}
	public void setTercaInicio(Date tercaInicio) {
		this.tercaInicio = tercaInicio;
	}
	public Date getTercaFim() {
		return tercaFim;
	}
	public void setTercaFim(Date tercaFim) {
		this.tercaFim = tercaFim;
	}
	public Date getQuartaInicio() {
		return quartaInicio;
	}
	public void setQuartaInicio(Date quartaInicio) {
		this.quartaInicio = quartaInicio;
	}
	public Date getQuartaFim() {
		return quartaFim;
	}
	public void setQuartaFim(Date quartaFim) {
		this.quartaFim = quartaFim;
	}
	public Date getQuintaInicio() {
		return quintaInicio;
	}
	public void setQuintaInicio(Date quintaInicio) {
		this.quintaInicio = quintaInicio;
	}
	public Date getQuintaFim() {
		return quintaFim;
	}
	public void setQuintaFim(Date quintaFim) {
		this.quintaFim = quintaFim;
	}
	public Date getSextaInicio() {
		return sextaInicio;
	}
	public void setSextaInicio(Date sextaInicio) {
		this.sextaInicio = sextaInicio;
	}
	public Date getSextaFim() {
		return sextaFim;
	}
	public void setSextaFim(Date sextaFim) {
		this.sextaFim = sextaFim;
	}
	public Date getSabadoInicio() {
		return sabadoInicio;
	}
	public void setSabadoInicio(Date sabadoInicio) {
		this.sabadoInicio = sabadoInicio;
	}
	public Date getSabadoFim() {
		return sabadoFim;
	}
	public void setSabadoFim(Date sabadoFim) {
		this.sabadoFim = sabadoFim;
	}
	public Date getDomingoInicio() {
		return domingoInicio;
	}
	public void setDomingoInicio(Date domingoInicio) {
		this.domingoInicio = domingoInicio;
	}
	public Date getDomingoFim() {
		return domingoFim;
	}
	public void setDomingoFim(Date domingoFim) {
		this.domingoFim = domingoFim;
	}
	

	
	

}
