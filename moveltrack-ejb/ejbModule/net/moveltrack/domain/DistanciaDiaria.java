package net.moveltrack.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class DistanciaDiaria extends BaseEntity {
	
	private static final long serialVersionUID = -3378138988214642824L;

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
    @Temporal(TemporalType.DATE)
    private Date dataComputada;

    @NotNull
    private double distanciaPercorrida;
    
    @ManyToOne
    private Veiculo veiculo;
     
	public Date getDataComputada() {
		return dataComputada;
	}
	public void setDataComputada(Date dataComputada) {
		this.dataComputada = dataComputada;
	}
	public double getDistanciaPercorrida() {
		return distanciaPercorrida;
	}
	public void setDistanciaPercorrida(double distanciaPercorrida) {
		this.distanciaPercorrida = distanciaPercorrida;
	}
	public Veiculo getVeiculo() {
		return veiculo;
	}
	public void setVeiculo(Veiculo veiculo) {
		this.veiculo = veiculo;
	}
	
    
 
     
}
