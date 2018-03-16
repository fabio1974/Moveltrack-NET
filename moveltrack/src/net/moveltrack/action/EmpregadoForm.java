package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.EmpregadoDao;
import net.moveltrack.dao.PerfilDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.PerfilTipo;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.PessoaStatus;
import net.moveltrack.domain.Usuario;
import net.moveltrack.util.Criptografia;
import net.moveltrack.util.Utils;
import net.moveltrack.util.ValidaCpf;


@Named
@SessionScoped
public class EmpregadoForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	EmpregadoDao dao;
	
	@Inject	PessoaDao pessoaDao;
	
	//@Inject EmpregadoDao empregadoDao;

	private Empregado empregado;
	private PerfilTipo tipo;
	
	private String action = Action.INSERT; 
	


	public EmpregadoForm() {
		System.out.println("Constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		if(empregado==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		empregado = new Empregado();
		Usuario usuario = new Usuario();
		try {
			usuario.setSenha(new Criptografia().calculaHash("1234"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		usuario.setAtivo(Boolean.TRUE);
		empregado.setUsuario(usuario);
		empregado.setDataCadastro(new Date());
		empregado.setStatus(PessoaStatus.ATIVO);
	}
	
	@Inject
	UsuarioDao usuarioDao;
	
	@Inject 
	PerfilDao perfilDao;
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			
			empregado.setUltimaAlteracao(new Date());
			empregado.getUsuario().setPerfil(perfilDao.findByTipo(tipo));
			empregado.getUsuario().setAtivo(empregado.getStatus()==PessoaStatus.ATIVO);
			
			if(action.equals(Action.INSERT)){
				usuarioDao.salvar(empregado.getUsuario());
				dao.salvar(empregado);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				usuarioDao.merge(empregado.getUsuario());
				dao.merge(empregado);
				operacaoSucesso();
			}
			empregadoTable.refreshPage();
			return "empregadoTable";
		}else
			return "empregadoForm";
	}
	
	@Inject
	EmpregadoTable empregadoTable;
	
	private String confirmaSenha1;
	private String confirmaSenha2;
	
	
	private Boolean validaSalvaSenha(){ 
		
		if(StringUtils.isBlank(confirmaSenha1)){
			operacaoErro("error.senhaConfirma.nula","confirmaSenha1",false);
			return false;
		}
		
		if(!confirmaSenha1.equals(confirmaSenha2)){
			operacaoErro("error.cliente.senhaConfirma.invalido","confirmaSenha2",false);
			return false;
		}
		return true;
	}
	
	
	
	@Transactional
	public String salvaSenha() {
		if(validaSalvaSenha()){
			empregado.getUsuario().setSenha(new Criptografia().calculaHash(confirmaSenha1));
			usuarioDao.merge(empregado.getUsuario());
			operacaoSucesso();
		}
		return "empregadoSenha";
	}	
	

	public String sair(){
		return "empregadoTable";
	}
	
	private Boolean validaGravacao() {
		if (empregado == null) {
			operacaoErro("error.empregado.null");
			return false;
		}
		
		if (StringUtils.isEmpty(empregado.getNome())) {
			operacaoErro("error.empregado.nome.nao.informado","nome", false);
			return false;
		}


		if (StringUtils.isEmpty(empregado.getCpf())) {
			operacaoErro("error.empregado.cpf.nao.informado","cpf", false);
			return false;
		} else if (!ValidaCpf.isValido(empregado.getCpf())) {
			operacaoErro("error.empregado.cpf.invalido","cpf", false);
			return false;
		}else if(isCpfDuplicado(empregado.getCpf())){
			operacaoErro("error.pessoa.cpf.duplicado","cpf",false);
			return false;
		}

		
		if(!StringUtils.isEmpty(empregado.getEmail()) && !Utils.validEmail(empregado.getEmail())){
			operacaoErro("error.empregado.email.invalido","email", false);
			return false;
		}
		
		if (StringUtils.isEmpty(empregado.getEndereco())) {
			operacaoErro("error.empregado.endereco.nao.informado","endereco", false);
			return false;
		}		

		if (StringUtils.isEmpty(empregado.getBairro())) {
			operacaoErro("error.empregado.bairro.nao.informado","bairro", false);
			return false;
		}		

		if (empregado.getMunicipio()==null) {
			operacaoErro("error.empregado.municipio.nao.informado","municipio", false);
			return false;
		}		
		
/*		if (StringUtils.isEmpty(empregado.getCep())) {
			operacaoErro("error.empregado.cep.nao.informado","cep", false);
			return false;
		}		
*/		
		if (StringUtils.isEmpty(empregado.getUsuario().getNomeUsuario())) {
			operacaoErro("error.usuario.nomeUsuario.nao.informado","nomeUsuario", false);
			return false;
		}		
		
		if(isUsuarioInternetDuplicado(empregado.getUsuario().getNomeUsuario())){
			operacaoErro("error.usuario.nomeUsuario.duplicado","nomeUsuario", false);
			return false;
		}
	
		if (StringUtils.isEmpty(empregado.getCelular1())) {
			operacaoErro("error.empregado.celular1.nao.informado","celular1", false);
			return false;
		}
		
		
		if (empregado.getDataCadastro()==null) {
			operacaoErro("error.empregado.dataCadastro.nao.informado","dataCadastro", false);
			return false;
		}
		
		if (empregado.getStatus()==null){
			operacaoErro("error.empregado.status.nao.informado","status", false);
			return false;
		}
		
		
		return true;
		

	}
	
	private boolean isUsuarioInternetDuplicado(String nomeUsuario) {
		Usuario us = usuarioDao.findByNomeUsuario(nomeUsuario);
		return us!=null && !us.equals(empregado.getUsuario());
	}
	
	private boolean isCpfDuplicado(String cpf) {
		Pessoa doBanco = pessoaDao.findByCpf(cpf);
		return doBanco!=null && !doBanco.equals(empregado);
	}
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		this.action = action;
	}


	public PerfilTipo[] getEmpregadoTipos(){
		
		return  new PerfilTipo[]{
				PerfilTipo.ADMINISTRADOR,
				PerfilTipo.FINANCEIRO,
				PerfilTipo.GERENTE_GERAL,
				PerfilTipo.GERENTE_ADM,
				PerfilTipo.INSTALADOR,
				PerfilTipo.VENDEDOR
				};
	}
	

	public Empregado getEmpregado() {
		return empregado;
	}

	public void setEmpregado(Empregado empregado) {
		this.empregado = empregado;
	}

	public PerfilTipo getTipo() {
		return tipo;
	}

	public void setTipo(PerfilTipo tipo) {
		this.tipo = tipo;
	}

	public PessoaStatus[] getStatuses(){
		return PessoaStatus.values();
	}

	public String getConfirmaSenha1() {
		return confirmaSenha1;
	}

	public void setConfirmaSenha1(String confirmaSenha1) {
		this.confirmaSenha1 = confirmaSenha1;
	}

	public String getConfirmaSenha2() {
		return confirmaSenha2;
	}

	public void setConfirmaSenha2(String confirmaSenha2) {
		this.confirmaSenha2 = confirmaSenha2;
	}


}