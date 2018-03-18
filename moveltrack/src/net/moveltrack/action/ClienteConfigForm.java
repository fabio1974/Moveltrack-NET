package net.moveltrack.action;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ClienteConfigDao;
import net.moveltrack.domain.ClienteConfig;
import net.moveltrack.util.Utils;


@Named
@SessionScoped
public class ClienteConfigForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	ClienteConfigDao dao;
	
	private ClienteConfig clienteConfig;
	private String action = Action.INSERT;

	


	public ClienteConfigForm() {
		System.out.println("Constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		if(clienteConfig==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		clienteConfig = new ClienteConfig();
	}
	
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			if(action.contains(Action.INSERT)){
				dao.salvar(clienteConfig);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(clienteConfig);
				operacaoSucesso();
			}
			return "veiculoForm";
		}else
			return "veiculoForm";
	}
	

	
	private Boolean validaGravacao() {

		if(!StringUtils.isEmpty(clienteConfig.getEmailAlarme()) && !Utils.validEmail(clienteConfig.getEmailAlarme())){
			operacaoErro("error.clienteConfig.emailAlarme.invalido","emailAlarme", false);
			return false;
		}
		
		return true;
	}
	
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		buildNewObject();
		this.action = action;
	}

	public ClienteConfig getClienteConfig() {
		return clienteConfig;
	}

	public void setClienteConfig(ClienteConfig clienteConfig) {
		this.clienteConfig = clienteConfig;
	}
}