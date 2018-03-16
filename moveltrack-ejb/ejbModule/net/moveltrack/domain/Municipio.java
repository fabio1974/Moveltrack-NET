package net.moveltrack.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.annotations.Expose;

@XmlRootElement
@Entity
public class Municipio extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 6175270972404302003L;

	@Id
	private Integer codigo;

	@Override
	public Integer getId() {
		return codigo;
	}

	@Override
	public void setId(Integer id) {
		this.codigo = id;
		
	}
	

	public Municipio() {

	}
	
	private String descricao;

	@ManyToOne
	private Uf uf;	


	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Basic(fetch=FetchType.LAZY)
	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}

	@Override
	public String toString() {
		return descricao + "-" + uf.getSigla();
	}


}    
 
