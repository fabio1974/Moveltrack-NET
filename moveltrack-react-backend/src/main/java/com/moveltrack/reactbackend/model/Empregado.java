package com.moveltrack.reactbackend.model;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.sun.istack.internal.NotNull;

@Entity
public class Empregado extends Pessoa {

	private static final long serialVersionUID = -1472169364362715694L;
	

	private double salario;

    @Override
    public String toString() {
        return getNome();
    }

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}


}
