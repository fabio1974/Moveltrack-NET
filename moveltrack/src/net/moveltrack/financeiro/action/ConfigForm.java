package net.moveltrack.financeiro.action;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.action.Action;
import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ConfigDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.domain.Config;
import net.moveltrack.domain.Operadora;
import net.moveltrack.security.LoginBean;





@Named
@SessionScoped
public class ConfigForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	ConfigDao dao;

	private Config config;


	private String action = Action.INSERT; 


	public ConfigForm() {
		System.out.println("config form constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("config form  Init ... ");
		if(config==null)
			buildNewObject();
	}
	
	

	@Inject
	ConfigDao configDao;
	
	@Inject 
	LoginBean loginBean;
	
	private void buildNewObject(){
		config = new Config();
	}
	
	@Inject 
	ConfigTable configTable;

	@Inject 
	BoletoTable boletoTable;	
	
	
	@Inject
	ContratoDao contratoDao;
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			if(action.equals(Action.INSERT)){
				dao.salvar(config);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(config);
				operacaoSucesso();
			}
			configTable.refreshPage();
			boletoTable.refreshPage();
			return "configTable";
		}else
			return "configForm";
	}
	

	public String sair(){
		return "configTable";
	}
	
	private Boolean validaGravacao() {
/*		if (config.getNumero() == null || config.getNumero().equals("") ) {
			operacaoErro("error.config.numero.nao.informado","numero",false);
			return false;
		}
		
		if(isNumeroConfigDuplicado(config.getNumero())){
			operacaoErro("error.config.numero.duplicado","numero",false);
			return false;
		}
		
		if(StringUtils.isNotBlank(config.getIccid())&& isIccidDuplicado(config.getIccid())){
			operacaoErro("error.config.iccid.duplicado","iccid",false);
			return false;
		}
		
		if (config.getOperadora() == null) {
			operacaoErro("error.config.operadora.nao.informado","operadora",false);
			return false;
		}
		
		if (config.getDataCadastro() == null) {
			operacaoErro("error.config.dataCadastro.nao.informado","dataCadastro",false);
			return false;
		}*/

		return true;
	}
	
	
/*	private boolean isIccidDuplicado(String iccid) {
		Config doBanco = dao.findByIccid(iccid);
		return (doBanco!=null && doBanco.getId()!= config.getId());
	}

	private boolean isNumeroConfigDuplicado(String numero) {
		Config doBanco = dao.findByNumero(numero);
		return (doBanco!=null && doBanco.getId()!= config.getId());
	}*/
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		this.action = action;
	}


	public Operadora[] getOperadoras(){
		return Operadora.values();
	}



	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}


}