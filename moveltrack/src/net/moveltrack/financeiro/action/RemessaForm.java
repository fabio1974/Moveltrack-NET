package net.moveltrack.financeiro.action;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.action.Action;
import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.RemessaDao;
import net.moveltrack.domain.Operadora;
import net.moveltrack.domain.Remessa;
import net.moveltrack.security.LoginBean;





@Named
@SessionScoped
public class RemessaForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	RemessaDao dao;

	private Remessa remessa;


	private String action = Action.INSERT; 


	public RemessaForm() {
		System.out.println("remessa form constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("remessa form  Init ... ");
		if(remessa==null)
			buildNewObject();
	}
	
	

	@Inject
	RemessaDao remessaDao;
	
	@Inject 
	LoginBean loginBean;
	
	private void buildNewObject(){
		remessa = new Remessa();
	}
	
	@Inject 
	RemessaTable remessaTable;

	@Inject 
	BoletoTable boletoTable;	
	
	
	@Inject
	ContratoDao contratoDao;
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			if(action.equals(Action.INSERT)){
				dao.salvar(remessa);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(remessa);
				operacaoSucesso();
			}
			remessaTable.refreshPage();
			boletoTable.refreshPage();
			return "remessaTable";
		}else
			return "remessaForm";
	}
	

	public String sair(){
		return "remessaTable";
	}
	
	private Boolean validaGravacao() {
/*		if (remessa.getNumero() == null || remessa.getNumero().equals("") ) {
			operacaoErro("error.remessa.numero.nao.informado","numero",false);
			return false;
		}
		
		if(isNumeroRemessaDuplicado(remessa.getNumero())){
			operacaoErro("error.remessa.numero.duplicado","numero",false);
			return false;
		}
		
		if(StringUtils.isNotBlank(remessa.getIccid())&& isIccidDuplicado(remessa.getIccid())){
			operacaoErro("error.remessa.iccid.duplicado","iccid",false);
			return false;
		}
		
		if (remessa.getOperadora() == null) {
			operacaoErro("error.remessa.operadora.nao.informado","operadora",false);
			return false;
		}
		
		if (remessa.getDataCadastro() == null) {
			operacaoErro("error.remessa.dataCadastro.nao.informado","dataCadastro",false);
			return false;
		}*/

		return true;
	}
	
	
/*	private boolean isIccidDuplicado(String iccid) {
		Remessa doBanco = dao.findByIccid(iccid);
		return (doBanco!=null && doBanco.getId()!= remessa.getId());
	}

	private boolean isNumeroRemessaDuplicado(String numero) {
		Remessa doBanco = dao.findByNumero(numero);
		return (doBanco!=null && doBanco.getId()!= remessa.getId());
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



	public Remessa getRemessa() {
		return remessa;
	}

	public void setRemessa(Remessa remessa) {
		this.remessa = remessa;
	}


}