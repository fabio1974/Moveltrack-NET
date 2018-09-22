package net.moveltrack.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.PermissaoDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.PerfilTipo;
import net.moveltrack.domain.Permissao;
import net.moveltrack.domain.Permission;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.Usuario;
import net.moveltrack.session.Navigator;
import net.moveltrack.util.Criptografia;

@Named
@SessionScoped
public class LoginBean extends BaseAction implements Serializable{

	private static final long serialVersionUID = -1609932210301058436L;

	private Usuario usuario;
	
	private boolean isLoggedIn;
	
	private Pessoa pessoaLogada;
	
	private Map<String,Permissao> permissoes = new HashMap<String,Permissao>();
	
	@ManagedProperty(value="#{navigator}")
	private Navigator navigator;
	
	@Inject UsuarioDao dao;

	@PostConstruct
	public void init() {
		if(usuario==null)
			buildBean();
	}

	private void buildBean() {
		usuario = new Usuario();
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	public String go(){
		if(loginValido())
			return "/admin/admin.jsf?faces-redirect=true";
		return "";
	}
	
	
	
	
	@Inject
	PermissaoDao permissaoDao;
	

	public boolean hasPermission(Permission permission){
		try{
			Permissao permissao = permissaoDao.findByDescricao(permission.toString());
			return  usuario.getPerfil().getPermissoes().contains(permissao) || usuario.getPermissoes().contains(permissao);
			
		}catch(Exception e){
			System.out.println("usuario "+usuario.getNomeUsuario()+ " não tem permissao "+permission.toString());
			return false;
		}
	}
	
	public boolean hasPermission(String permission){
		try{
			return permissoes.containsKey(permission);
		}catch(Exception e){
			System.out.println("usuario "+usuario.getNomeUsuario()+ " não tem permissao "+permission);
			return false;
		}

	}
	
	

	private Boolean loginValido() {
		
		
		if (usuario.getNomeUsuario() == null || usuario.getNomeUsuario().equals("") ) {
			operacaoErro("error.usuario.nome.nao.informado","nomeUsuario",false);
			return false;
		}
		
		if (usuario.getSenha() == null || usuario.getSenha().equals("") ) {
			operacaoErro("error.usuario.senha.nao.informado","senha",false);
			return false;
		}
		
		Usuario userDB = dao.findByNomeUsuario(usuario.getNomeUsuario());

		if(userDB==null){
			operacaoErro("error.usuario.nao.cadastrado.no.sistema","nomeUsuario",false);
			return false;
		}
		
		if(!new Criptografia().calculaHash(usuario.getSenha()).equals(userDB.getSenha())){
			operacaoErro("error.login.invalido",null,false);
			return false;
		}
		
		if(!userDB.isAtivo()){
			operacaoErro("error.usuario.temporariamente.inativo",null,false);
			return false;
		}

		setUsuario(userDB);
		setLoggedIn(true);
		setPessoaLogada(pessoaDao.findByUsuario(userDB));
		for (Permissao permissao : usuario.getPerfil().getPermissoes()) {
			permissoes.put(permissao.getDescricao(),permissao);
		}
		
		for (Permissao permissao : usuario.getPermissoes()) {
			permissoes.put(permissao.getDescricao(),permissao);
		}

		return true;
	}
	
	

	private Locale locale;
	
	
	
	
	public void setLocale(Locale locale) {
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}

	public Locale getLocale() {
		if(locale==null)
			locale = FacesContext.getCurrentInstance().getViewRoot().getLocale(); 
		return locale;
	}


	public String logout(){
		setLoggedIn(false);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		session.invalidate();
		return "/index.jsf?faces-redirect=true";
	}
	
	public void invalidateSession(){
		FacesContext facesContext = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
		session.invalidate();
	}

	

	public Pessoa getPessoaLogada() {
		return pessoaLogada;
	}

	public void setPessoaLogada(Pessoa pessoaLogada) {
		this.pessoaLogada = pessoaLogada;
	}

	public boolean isLoggedIn() {
		return isLoggedIn;
	}

	public void setLoggedIn(boolean isLoggedIn) {
		this.isLoggedIn = isLoggedIn;
	}

	public Navigator getNavigator() {
		return navigator;
	}

	public void setNavigator(Navigator navigator) {
		this.navigator = navigator;
	}
	
	public boolean isEmpregado(){
		return pessoaLogada instanceof Empregado;
	}
	
	public boolean isEmpregadoGerenteGeral(){
		return pessoaLogada instanceof Empregado  && pessoaLogada.getUsuario().getPerfil().getTipo() == PerfilTipo.GERENTE_GERAL;	
	}
	
	public boolean isEmpregadoAdministrador(){
		return pessoaLogada instanceof Empregado  && pessoaLogada.getUsuario().getPerfil().getTipo() == PerfilTipo.ADMINISTRADOR;	
	}

	
	public boolean isCliente(){
		return pessoaLogada instanceof Cliente;
	}


	@Inject
	PessoaDao pessoaDao;
	public String getUsuarioNome() {
		try{
				return pessoaDao.findByUsuario(usuario).getNome();
		}catch(Exception e){
			return null;
		}
	}


	
}
