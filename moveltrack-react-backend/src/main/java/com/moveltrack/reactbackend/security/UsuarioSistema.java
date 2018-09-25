package com.moveltrack.reactbackend.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.moveltrack.reactbackend.model.Contrato;
import com.moveltrack.reactbackend.model.Usuario;

public class UsuarioSistema extends User {

	private static final long serialVersionUID = 1L;

	private Contrato contrato;

	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities,Contrato contrato) {
		super(usuario.getNomeUsuario(), usuario.getSenha(), authorities);
		this.contrato = contrato;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	
}