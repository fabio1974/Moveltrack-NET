package com.moveltrack.cartaoprograma.model;

//@Entity
//@Table(name = "tb_usu_usuario")
public class Usuario {

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "usu_id_usuario")
	private Long id;

//	@Column(name = "usu_ds_nome")
	private String nome;
//
//	@Column(name = "usu_ds_nome_guerra")
//	private String nomeGuerra;
//
//	@Column(name = "usu_ds_matricula")
//	private String matricula;

//	@Column(name = "usu_ds_cpf")
	private String cpf;

//	@Column(name = "usu_ds_email")
//	private String email;

//	@Column(name = "usu_ds_password")
	private String password;

//	@Column(name = "usu_celular")
//	private String celular;
//	
//	@ManyToOne
//	@JoinColumn(name = "uf_sigla")
//	private Uf uf;
//
//	@ManyToOne
//	@JoinColumn(name = "org_id_orgao")
//	private Orgao orgao;
//	
//	@JsonIgnore
//	@OneToMany(mappedBy="usuario", cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
//	private List<UsuarioGrupo> usuarioGrupo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
//
//	public String getNomeGuerra() {
//		return nomeGuerra;
//	}
//
//	public void setNomeGuerra(String nomeGuerra) {
//		this.nomeGuerra = nomeGuerra;
//	}
//
//	public String getMatricula() {
//		return matricula;
//	}
//
//	public void setMatricula(String matricula) {
//		this.matricula = matricula;
//	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

//	public String getCelular() {
//		return celular;
//	}
//
//	public void setCelular(String celular) {
//		this.celular = celular;
//	}
//
//	public Uf getUf() {
//		return uf;
//	}
//
//	public void setUf(Uf uf) {
//		this.uf = uf;
//	}
//
//	public Orgao getOrgao() {
//		return orgao;
//	}
//
//	public void setOrgao(Orgao orgao) {
//		this.orgao = orgao;
//	}
//
//	public List<UsuarioGrupo> getUsuarioGrupo() {
//		return usuarioGrupo;
//	}
//
//	public void setUsuarioGrupo(List<UsuarioGrupo> usuarioGrupo) {
//		this.usuarioGrupo = usuarioGrupo;
//	}
//
//	public Set<Funcionalidade> getFuncionalidades() {
//		Set<Funcionalidade> funcionalidades = new HashSet<>();
//		usuarioGrupo.forEach(ug -> funcionalidades.addAll(ug.getGrupo().getFuncionalidades()));
//
//		return funcionalidades;
//	}
}