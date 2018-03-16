package net.moveltrack.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.sun.istack.internal.NotNull;


@Entity
public class Perfil extends BaseEntity {
	
	private static final long serialVersionUID = -1633936031016219418L;

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

    
    //private String descricao;

    /**
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permissao> permissoes = new HashSet<Permissao>();


/*    public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}*/
    
    
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
	
	
/*	@Override
	public String toString(){
		return descricao;
	}*/


}