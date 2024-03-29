package net.moveltrack.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author fabio.barros
 */
//@XmlRootElement
@Entity
public class Uf extends BaseEntity{

	private static final long serialVersionUID = -2099040311102882906L;


	private Integer id;

	private String sigla;

	private String descricao;

	private boolean horarioVerao;

	private Integer gmt;

	public Uf() {

	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Uf(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isHorarioVerao() {
		return horarioVerao;
	}

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public void setHorarioVerao(boolean horarioVerao) {
		this.horarioVerao = horarioVerao;
	}

	public Integer getGmt() {
		return gmt;
	}

	public void setGmt(Integer gmt) {
		this.gmt = gmt;
	}

	@Override
	public String toString() {
		return sigla;
	}
}