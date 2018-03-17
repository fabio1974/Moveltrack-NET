package net.moveltrack.rest.antigo;

import java.util.List;



public class ClienteApp {

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	public List<Movel> getMoveis() {
		return moveis;
	}
	public void setMoveis(List<Movel> moveis) {
		this.moveis = moveis;
	}


	int id;
	String nome;
	String senha;
	String cpf;
	String telefone;
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}

	String userLogin;
	
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	boolean ativo;
	List<Movel> moveis;
	
	
	public String getPessoaTipo() {
		return pessoaTipo;
	}
	public void setPessoaTipo(String pessoaTipo) {
		this.pessoaTipo = pessoaTipo;
	}

	private String  pessoaTipo;
	
}
