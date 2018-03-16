package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.dao.MotoristaDao;
import net.moveltrack.dao.PerfilDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.domain.CategoriaCnh;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.PessoaStatus;
import net.moveltrack.security.LoginBean;
import net.moveltrack.util.Utils;
import net.moveltrack.util.ValidaCpf;


@Named
public class MotoristaForm extends ConversationBaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;


	@Inject
	MotoristaDao dao;
	
	@Inject
	PessoaDao pessoaDao;
	
	@Inject
	LoginBean loginBean;

	private Motorista motorista;
	private String action = Action.INSERT; 
	


	public MotoristaForm() {
		System.out.println("Constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		if(motorista==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		motorista = new Motorista();
		Pessoa pessoa = loginBean.getPessoaLogada();
		if(pessoa instanceof Cliente)
			motorista.setPatrao((Cliente)pessoa);
		motorista.setStatus(PessoaStatus.ATIVO);
		motorista.setDataCadastro(new Date());
	}
	
	@Inject
	UsuarioDao usuarioDao;
	
	@Inject 
	PerfilDao perfilDao;
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			motorista.setUltimaAlteracao(new Date());
			//motorista.getUsuario().setPerfil(perfilDao.findByDescricao("MOTORISTA"));
			if(action.equals(Action.INSERT)){
				//usuarioDao.salvar(motorista.getUsuario());
				dao.salvar(motorista);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				//usuarioDao.merge(motorista.getUsuario());
				dao.merge(motorista);
				operacaoSucesso();
			}
			motoristaTable.refreshPage();
			return "motoristaTable";
		}else
			return "motoristaForm";
	}
	
	@Inject
	MotoristaTable motoristaTable;

	public String sair(){
		return "motoristaTable";
	}
	
	private Boolean validaGravacao() {
		if (motorista == null) {
			operacaoErro("error.motorista.null");
			return false;
		}
		
		if (StringUtils.isEmpty(motorista.getNome())) {
			operacaoErro("error.motorista.nome.nao.informado","nome", false);
			return false;
		}


		if (StringUtils.isEmpty(motorista.getCpf())) {
			operacaoErro("error.motorista.cpf.nao.informado","cpf", false);
			return false;
		} else if (!ValidaCpf.isValido(motorista.getCpf())) {
			operacaoErro("error.motorista.cpf.invalido","cpf", false);
			return false;
		}else if(isCpfDuplicado(motorista.getCpf())){
			operacaoErro("error.motorista.cpf.duplicado","cpf",false);
			return false;
		}
		
		if (motorista.getValidadeCnh()==null) {
			operacaoErro("error.motorista.validadeCnh.nao.informado","validadeCnh", false);
			return false;
		}
		
		if (motorista.getCategoriaCnh()==null) {
			operacaoErro("error.motorista.categoriaCnh.nao.informado","categoriaCnh", false);
			return false;
		}

		
		if(!StringUtils.isEmpty(motorista.getEmail()) && !Utils.validEmail(motorista.getEmail())){
			operacaoErro("error.motorista.email.invalido","email", false);
			return false;
		}
		
/*		if (StringUtils.isEmpty(motorista.getEndereco())) {
			operacaoErro("error.motorista.endereco.nao.informado","endereco", false);
			return false;
		}		

		if (StringUtils.isEmpty(motorista.getBairro())) {
			operacaoErro("error.motorista.bairro.nao.informado","bairro", false);
			return false;
		}		

		if (motorista.getMunicipio()==null) {
			operacaoErro("error.motorista.municipio.nao.informado","municipio", false);
			return false;
		}*/		
		
/*		if (StringUtils.isEmpty(motorista.getCep())) {
			operacaoErro("error.motorista.cep.nao.informado","cep", false);
			return false;
		}	*/	
		
/*		if (StringUtils.isEmpty(motorista.getUsuario().getNomeUsuario())) {
			operacaoErro("error.usuario.nomeUsuario.nao.informado","nomeUsuario", false);
			return false;
		}	*/	
		
/*		if(isUsuarioInternetDuplicado(motorista.getUsuario().getNomeUsuario())){
			operacaoErro("error.usuario.nomeUsuario.duplicado","nomeUsuario", false);
			return false;
		}*/
	
		if (StringUtils.isEmpty(motorista.getCelular1())) {
			operacaoErro("error.motorista.celular1.nao.informado","celular1", false);
			return false;
		}
		
		
		if (motorista.getDataCadastro()==null) {
			operacaoErro("error.motorista.dataCadastro.nao.informado","dataCadastro", false);
			return false;
		}
		

		
		return true;
		

	}
	
/*	private boolean isUsuarioInternetDuplicado(String nomeUsuario) {
		Usuario us = usuarioDao.findByNomeUsuario(nomeUsuario);
		return (us!=null && us.getId()!= motorista.getUsuario().getId());
	}*/
	
	private boolean isCpfDuplicado(String cpf) {
		Pessoa doBanco = pessoaDao.findByCpf(cpf);
		return (doBanco!=null && doBanco.getId()!= motorista.getId());
	}
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		this.action = action;
	}


	public Motorista getMotorista() {
		return motorista;
	}

	public void setMotorista(Motorista motorista) {
		this.motorista = motorista;
	}

	public PessoaStatus[] getStatuses(){
		return PessoaStatus.values();
	}
	
	public CategoriaCnh[] getCategoriasCnh(){
		return CategoriaCnh.values();
	}



}