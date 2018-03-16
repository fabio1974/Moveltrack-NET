package net.moveltrack.financeiro.action;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.action.Action;
import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ConfigDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.LancamentoDao;
import net.moveltrack.dao.RemessaDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.HouveBaixa;
import net.moveltrack.domain.Lancamento;
import net.moveltrack.domain.LancamentoFormaPagamento;
import net.moveltrack.domain.LancamentoTipo;
import net.moveltrack.domain.LancamentoStatus;
import net.moveltrack.domain.Remessa;
import net.moveltrack.domain.TipoDeCobranca;
import net.moveltrack.security.LoginBean;

@Named
@SessionScoped
public class LancamentoForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	LancamentoDao dao;

	private Lancamento lancamento;
	private String operadorLabel;
	private String solicitanteLabel;
	
	private String action = Action.INSERT; 


	public LancamentoForm() {
		System.out.println("boleto form constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("boleto form  Init ... ");
		if(lancamento==null)
			buildNewObject();
	}
	
	

	@Inject
	LancamentoTable lancamentoTable;
	
	@Inject 
	LoginBean loginBean;
	
	@Inject ConfigDao configDao;
	
	private void buildNewObject(){
		lancamento = new Lancamento();
		lancamento.setData(new Date());
		lancamento.setOperador((Empregado)loginBean.getPessoaLogada());
		lancamento.setStatus(LancamentoStatus.ATIVO);
	}
	
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {

			if(action.equals(Action.INSERT)){
				dao.salvar(lancamento);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				dao.merge(lancamento);
				operacaoSucesso();

			}
			lancamentoTable.refreshPage();
			return "lancamentoTable";
		}else
			return "lancamentoForm";
	}
	


	
	private Boolean validaGravacao() {
		
		if (lancamento.getOperacao() == null) {
			operacaoErro("error.lancamento.operacao.nao.informado","operacao",false);
			return false;
		}
		
		if (lancamento.getOperacao()==LancamentoTipo.RECEBIMENTO_DE_CLIENTE && lancamento.getOrdemDeServico()==null) {
			operacaoErro("error.lancamento.ordemdeservico.nao.informado","operacao",false);
			return false;
		}

		
		if (lancamento.getSolicitante() == null) {
			operacaoErro("error.lancamento.solicitante.nao.informado","solicitante",false);
			return false;
		}

		if (lancamento.getOperador() == null) {
			operacaoErro("error.lancamento.operador.nao.informado","operador",false);
			return false;
		}
		

		if (lancamento.getValor() <= 0) {
			operacaoErro("error.lancamento.valor.diferente.de.zero","valor",false);
			return false;
		}
		
		if (lancamento.getData() == null) {
			operacaoErro("error.lancamento.data.nao.informado","data",false);
			return false;
		}
		

		if (lancamento.getOperacao() == LancamentoTipo.RECEBIMENTO_DE_CLIENTE && lancamento.getFormaPagamento()==null) {
			operacaoErro("error.lancamento.formaPagamento.nao.informado","formaPagamento",false);
			return false;
		}

		
		
		if (StringUtils.isBlank(lancamento.getObservacao()) && (lancamento.getOperacao()==LancamentoTipo.RECEBIMENTO_DE_CLIENTE || lancamento.getOperacao()==LancamentoTipo.GASTO_DE_MATERIAL)) {
			operacaoErro("error.lancamento.observacao.nao.informado","observacao",false);
			return false;
		}
		
		if (lancamento.getStatus() == null) {
			operacaoErro("error.lancamento.sem.status","status",false);
			return false;
		}
		
		
		if (lancamento.getOperacao()==LancamentoTipo.RECEBIMENTO_DE_CLIENTE && lancamento.getHouveBaixa() == null) {
			operacaoErro("error.lancamento.sem.houveBaixa","houveBaixa",false);
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

	
	public LancamentoTipo[] getOperacoes(){
		return LancamentoTipo.values();
	}
	
	public HouveBaixa[] getHouveBaixas(){
		return HouveBaixa.values();
	}
	
	public LancamentoStatus[] getStatuses(){
		return LancamentoStatus.values();
	}
	
	public LancamentoFormaPagamento[] getFormaPagamentos(){
		return LancamentoFormaPagamento.values();
	}

	public String getOperadorLabel() {
		if(lancamento.getOperacao()==null)
			return "Registrado por...";
		if(lancamento.getOperacao() == LancamentoTipo.DEVOLUCAO_DE_DINHEIRO)
			return "Entrega registrada por...";
		else if(lancamento.getOperacao() == LancamentoTipo.RECEBIMENTO_DE_CLIENTE)
			return "Recebimento registrado por...";
		else if(lancamento.getOperacao() == LancamentoTipo.VALE)
			return "Vale registrado por...";
		else if(lancamento.getOperacao() == LancamentoTipo.GASTO_DE_MATERIAL)
			return "Gasto registrado por...";
		else
			return "Operador";
	}
	
	
	

	public void setOperadorLabel(String operadorLabel) {
		this.operadorLabel = operadorLabel;
	}
	

	public String getSolicitanteLabel() {
		if(lancamento.getOperacao()==null)
			return "Titular";
		if(lancamento.getOperacao() == LancamentoTipo.DEVOLUCAO_DE_DINHEIRO)
			return "Dinheiro devolvido à Moveltrack por...";
		else if(lancamento.getOperacao() == LancamentoTipo.RECEBIMENTO_DE_CLIENTE)
			return "Dinheiro recebido por...";
		else if(lancamento.getOperacao() == LancamentoTipo.VALE)
			return "Vale solicitado por...";
		else if(lancamento.getOperacao() == LancamentoTipo.GASTO_DE_MATERIAL)
			return "Gasto realizado por...";
		
		else
			return "Titular";

	}

	public void setSolicitanteLabel(String solicitanteLabel) {
		this.solicitanteLabel = solicitanteLabel;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}



}