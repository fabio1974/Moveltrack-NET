package com.moveltrack.reactbackend.model;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.sun.istack.internal.NotNull;

@Entity
public class Motorista extends Pessoa {

	private static final long serialVersionUID = -1472169364362715694L;


    @Override
    public String toString() {
        return getNome();
    }

    
/*    @NotNull
    @Enumerated(EnumType.STRING)
    private MotoristaStatus status;
*/    
    
    @ManyToOne
    private Cliente patrao;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date validadeCnh;
    
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private CategoriaCnh categoriaCnh;
    
    


	public CategoriaCnh getCategoriaCnh() {
		return categoriaCnh;
	}


	public void setCategoriaCnh(CategoriaCnh categoriaCnh) {
		this.categoriaCnh = categoriaCnh;
	}


/*	public MotoristaStatus getStatus() {
		return status;
	}


	public void setStatus(MotoristaStatus status) {
		this.status = status;
	}*/



	public Cliente getPatrao() {
		return patrao;
	}


	public void setPatrao(Cliente patrao) {
		this.patrao = patrao;
	}


	public Date getValidadeCnh() {
		return validadeCnh;
	}


	public void setValidadeCnh(Date validadeCnh) {
		this.validadeCnh = validadeCnh;
	}
    
    
    

    

}
