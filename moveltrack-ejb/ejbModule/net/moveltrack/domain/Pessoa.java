package net.moveltrack.domain;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;



@Entity
public class Pessoa extends BaseEntity{
	
	private static final long serialVersionUID = 6156647507718897901L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}


    @Size(max = 25)
    private String cpf;
    
    @Column(unique = true)
    @Size(max = 25)
    private String cnpj;

    @Enumerated(EnumType.STRING)
    private PessoaStatus status;  

    @NotNull
    @Size(max = 60)
    private String nome;

    private String email;    
    
    private String logoFile;    


    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaAlteracao;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;    
    
    @ManyToOne
    private Pessoa alterador;
    
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataNascimento;


    @Size(max = 40)
    private String telefoneFixo;


    @Size(max = 40)
    private String celular1;
    

    @Size(max = 40)
    private String celular2;

    
    @NotNull
    @Size(max = 60)
    private String endereco;

    @NotNull
    @Size(max = 5)
    private String numero;

    @Size(max = 40)
    private String complemento;


    @ManyToOne
    private Municipio municipio;

    @Size(max = 30)
    private String cep;

    @NotNull
    private String bairro;
    
    
    @ManyToOne
    private Usuario usuario;


	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getCnpj() {
		return cnpj;
	}
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public PessoaStatus getStatus() {
		return status;
	}
	public void setStatus(PessoaStatus status) {
		this.status = status;
	}
	public Date getDataNascimento() {
		return dataNascimento;
	}
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	
	

	public Date getUltimaAlteracao() {
		return ultimaAlteracao;
	}
	public void setUltimaAlteracao(Date ultimaAlteracao) {
		this.ultimaAlteracao = ultimaAlteracao;
	}
	public Pessoa getAlterador() {
		return alterador;
	}
	public void setAlterador(Pessoa alterador) {
		this.alterador = alterador;
	}
	public String getTelefoneFixo() {
		return telefoneFixo;
	}
	public void setTelefoneFixo(String telefoneFixo) {
		this.telefoneFixo = telefoneFixo;
	}
	public String getCelular1() {
		return celular1;
	}
	public void setCelular1(String celular1) {
		this.celular1 = celular1;
	}
	public String getCelular2() {
		return celular2;
	}
	public void setCelular2(String celular2) {
		this.celular2 = celular2;
	}
	public String getEndereco() {
		return endereco;
	}


	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}


	public String getNumero() {
		return numero;
	}


	public void setNumero(String numero) {
		this.numero = numero;
	}


	public String getComplemento() {
		return complemento;
	}


	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}


	public String getCep() {
		return cep;
	}


	public void setCep(String cep) {
		this.cep = cep;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}


	public Usuario getUsuario() {
		return usuario;
	}


	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	

	public Municipio getMunicipio() {
		return municipio;
	}
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}
	public Date getDataCadastro() {
		return dataCadastro;
	}
	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	@Override
    public String toString() {
		if(this instanceof Cliente){
			Cliente cli = (Cliente)this;
			return cli.toString();
		}
        return nome;
    }
	
	@Transient
	private String nomeAbreviado;
	
	public String getNomeAbreviado(){
		if(nome.length()>20)
			return nome.substring(0,19);
		return nome;
	}
  
	
    
/*    public boolean hasPermission(Permission p) {
    	if(pessoaTipo == null)
    		return false;
        for (Permissao permissao : pessoaTipo.getPermissoes()) {
            if (permissao.getPermission() == p) return true;
        }
        return false;
    }
*/    
    
    
    public void setNomeAbreviado(String nomeAbreviado) {
		this.nomeAbreviado = nomeAbreviado;
	}
	public String getLogoFile() {
    	if(logoFile==null)
    		logoFile = "logo_azul_221_57.png";
		return logoFile;
	}
	public void setLogoFile(String logoFile) {
		this.logoFile = logoFile;
	}
	
	
	public boolean isAdministrador() {
    		return this.getUsuario().getPerfil().getTipo() == PerfilTipo.ADMINISTRADOR;
    }
	
    

}
