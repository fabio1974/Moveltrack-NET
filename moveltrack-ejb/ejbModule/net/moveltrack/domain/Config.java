package net.moveltrack.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class Config extends BaseEntity {
	
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
	
    private double multa;
    
    private double juros;
    
    private boolean migrado;

	public double getMulta() {
		return multa;
	}
	public void setMulta(double multa) {
		this.multa = multa;
	}
	public double getJuros() {
		return juros;
	}
	public void setJuros(double juros) {
		this.juros = juros;
	}
	public boolean isMigrado() {
		return migrado;
	}
	public void setMigrado(boolean migrado) {
		this.migrado = migrado;
	}
	
	
    

}