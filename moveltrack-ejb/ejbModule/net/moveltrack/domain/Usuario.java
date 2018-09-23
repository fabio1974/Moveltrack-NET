package net.moveltrack.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.swing.text.MaskFormatter;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

@XmlRootElement
@Entity
public class Usuario extends BaseEntity {

	private static final long serialVersionUID = 9140631774344181539L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 50, nullable = false)
	private String senha;
	
	@Transient
	private String pwd;

	
	@Transient
	private String confirmaSenha;

	@Column(length = 50)
	private String email;

	@Column(nullable = false)
	private boolean ativo;

	@Column(length = 25, unique = true, nullable = false)
	private String nomeUsuario;

	@Temporal(TemporalType.TIMESTAMP)
	private Date ultimoAcesso;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private Perfil perfil;
	
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Permissao> permissoes = new HashSet<Permissao>();

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}
	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	
	public Usuario() {

	}
	
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public Perfil getPerfil() {
		return perfil;
	}


	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		if(senha!=null)
			senha = senha.trim();
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}



	public boolean isAtivo() {
		return ativo;
	}


	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		if(nomeUsuario!=null)
			nomeUsuario = nomeUsuario.trim();
		this.nomeUsuario = nomeUsuario;
	}

	public Date getUltimoAcesso() {
		return ultimoAcesso;
	}

	public void setUltimoAcesso(Date ultimoAcesso) {
		this.ultimoAcesso = ultimoAcesso;
	}



	public String getConfirmaSenha() {
		return confirmaSenha;
	}



	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}


	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	
	public boolean acessoFinanceiro(){
		return perfil.getTipo() == PerfilTipo.ADMINISTRADOR ||
				  perfil.getTipo() == PerfilTipo.FINANCEIRO ||
					 perfil.getTipo() == PerfilTipo.GERENTE_ADM;
	}	
	
	
/*	public List<UsuarioPermissao> getUsuarioPermissoes() {
		return usuarioPermissoes;
	}

	public void setUsuarioPermissoes(List<UsuarioPermissao> usuarioPermissoes) {
		this.usuarioPermissoes = usuarioPermissoes;
	}*/

/*	@Transient
	public String getCpfFormatado() {

		if (StringUtils.isBlank(nomeUsuario)) {
			return "";
		}

		MaskFormatter mask;
		try {
			mask = new MaskFormatter("###.###.###-##");
			mask.setValueContainsLiteralCharacters(false);
			return mask.valueToString(nomeUSuario);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return cpf;
	}*/
}