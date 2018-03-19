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
import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ClienteConfig;
import net.moveltrack.domain.Permission;
import net.moveltrack.security.LoginBean;
import net.moveltrack.util.Utils;


@Named
@SessionScoped
public class ClienteConfigForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	ClienteConfigDao dao;
	
	private ClienteConfig clienteConfig;
	private String action = Action.INSERT;
	@Inject LoginBean loginBean;
	@Inject	PessoaDao pessoaDao;
	@Inject ClienteDao clienteDao;


	public ClienteConfigForm() {
		System.out.println("Constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		if(loginBean.hasPermission(Permission.CLIENTE_VER_PROPRIO)){
			Cliente cliente = (Cliente)clienteDao.findByUsuario(loginBean.getUsuario());
			if(cliente.getClienteConfig()==null) {
				clienteConfig = new ClienteConfig();
				clienteConfig.setEmailAlarme(cliente.getEmailAlarme());
				dao.salvar(clienteConfig);
				cliente.setClienteConfig(clienteConfig);
				clienteDao.merge(cliente);
			}else {
				clienteConfig = cliente.getClienteConfig();
			}
			
		}
	}
	
	private void buildNewObject(){
		clienteConfig = new ClienteConfig();
	}
	
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
				dao.merge(clienteConfig);
				Cliente cliente = (Cliente)clienteDao.findByUsuario(loginBean.getUsuario());
				cliente.setEmailAlarme(clienteConfig.getEmailAlarme());
				clienteDao.merge(cliente);
				operacaoSucesso();
		}
		return "clienteConfigForm";
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