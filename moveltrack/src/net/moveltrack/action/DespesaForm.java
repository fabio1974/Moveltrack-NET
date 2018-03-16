package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.DespesaDao;
import net.moveltrack.domain.Despesa;
import net.moveltrack.domain.DespesaStatus;
import net.moveltrack.domain.DespesaTipo;


@Named
@SessionScoped
public class DespesaForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	DespesaDao dao;

	private Despesa despesa;
	private String action = Action.INSERT; 


	public DespesaForm() {
		System.out.println("despesa form constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("despesa form  Init ... ");
		if(despesa==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		despesa = new Despesa();
		despesa.setDataDaDespesa(new Date());
	}
	
	@Inject 
	DespesaTable despesaTable;
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			if(action.equals(Action.INSERT)){
				dao.salvar(despesa);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(despesa);
				operacaoSucesso();
			}
			despesaTable.refreshPage();
			return "despesaTable";
		}else
			return "despesaForm";
	}
	

	public String sair(){
		return "despesaTable";
	}
	
	private Boolean validaGravacao() {
		if (despesa.getDataDaDespesa() == null) {
			operacaoErro("error.despesa.dataDaDespesa.nao.informado","dataDaDespesa",false);
			return false;
		}
		
		if (despesa.getValor() <= 0) {
			operacaoErro("error.despesa.valor.nao.informado","valor",false);
			return false;
		}
		
		if (despesa.getTipoDeDespesa() == null) {
			operacaoErro("error.despesa.tipoDespesa.nao.informado","tipoDespesa",false);
			return false;
		}
		
		if (despesa.getStatus() == null) {
			operacaoErro("error.despesa.status.nao.informado","tipoDespesa",false);
			return false;
		}

		return true;
	}
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		this.action = action;
	}


	public DespesaTipo[] getTipoDeDespesas(){
		return DespesaTipo.values();
	}
	
	public DespesaStatus[] getStatuses(){
		return DespesaStatus.values();
	}

	public Despesa getDespesa() {
		return despesa;
	}

	public void setDespesa(Despesa despesa) {
		this.despesa = despesa;
	}
	


}