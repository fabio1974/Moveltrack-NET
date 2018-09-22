package com.moveltrack.reactbackend.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.sun.istack.internal.NotNull;


@Entity
public class Perfil {
	


	@Id
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

    @NotNull
    @Enumerated(EnumType.STRING)
    private PerfilTipo tipo;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="perfil_permissao")
    private Set<Permissao> permissoes = new HashSet<Permissao>();

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}
	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}
	public PerfilTipo getTipo() {
		return tipo;
	}
	public void setTipo(PerfilTipo tipo) {
		this.tipo = tipo;
	}
	

}