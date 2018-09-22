package com.moveltrack.reactbackend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@Entity
public class Permissao {

	private static final long serialVersionUID = 4409549347552909844L;

	@Id
	private Integer id;


	@Column(length = 40, nullable = false,unique=true)
	private String descricao;

	public Permissao() {

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

/*	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
*/
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	@Override
	public String toString(){
		return descricao;
	}

/*	public List<UsuarioPermissao> getUsuarioPermissoes() {
		return usuarioPermissoes;
	}

	public void setUsuarioPermissoes(List<UsuarioPermissao> usuarioPermissoes) {
		this.usuarioPermissoes = usuarioPermissoes;
	}
*/}