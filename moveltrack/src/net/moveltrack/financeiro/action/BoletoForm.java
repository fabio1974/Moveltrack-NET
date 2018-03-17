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
import net.moveltrack.dao.IuguDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.dao.RemessaDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.Iugu;
import net.moveltrack.domain.MBoleto;
import net.moveltrack.domain.MBoletoStatus;
import net.moveltrack.domain.MBoletoTipo;
import net.moveltrack.domain.Operadora;
import net.moveltrack.domain.Remessa;
import net.moveltrack.domain.TipoDeCobranca;
import net.moveltrack.security.LoginBean;
import net.moveltrack.util.IuguUtils;

@Named
@SessionScoped
public class BoletoForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	MBoletoDao dao;

	private MBoleto mBoleto;
	private Cliente cliente;
	private Remessa remessa;
	private String action = Action.INSERT; 
	

	public BoletoForm() {
		System.out.println("boleto form constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("boleto form  Init ... ");
		if(mBoleto==null)
			buildNewObject();
	}
	
	

	@Inject
	RemessaDao remessaDao;
	
	@Inject 
	LoginBean loginBean;
	
	@Inject ConfigDao configDao;
	
	private void buildNewObject(){
		
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR,1);
		mBoleto = new MBoleto();
		mBoleto.setDataEmissao(new Date());
		mBoleto.setDataVencimento(c.getTime());
		try{mBoleto.setEmissor((Empregado)loginBean.getPessoaLogada());}catch(Exception e){}
		mBoleto.setSituacao(MBoletoStatus.EMITIDO);
		mBoleto.setTipo(MBoletoTipo.AVULSO);
		mBoleto.setMulta(configDao.findById(1).getMulta());
		mBoleto.setJuros(configDao.findById(1).getJuros());
		mBoleto.setTipoDeCobranca(TipoDeCobranca.COM_REGISTRO);
	}
	
	@Inject 
	BoletoTable boletoTable;
	
	@Inject 
	RemessaTable remessaTable;
	
	
	@Inject
	ContratoDao contratoDao;
	
	@Inject IuguDao iuguDao;
	@Inject IuguUtils iuguUtils;
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			mBoleto.setContrato(contratoDao.findByCliente(cliente));
			mBoleto.setMulta(configDao.findById(1).getMulta());
			mBoleto.setJuros(configDao.findById(1).getJuros());
			

			if(action.equals(Action.INSERT)){
				mBoleto.setNossoNumero(dao.getProximoNossoNumero(mBoleto.getTipoDeCobranca()));
				if(mBoleto.getTipoDeCobranca()==TipoDeCobranca.COM_REGISTRO)
					mBoleto.setRemessa(remessaDao.getRemessaAtual());

				Iugu iuguData = iuguUtils.createIuguInvoice(mBoleto);
				mBoleto.setIugu(iuguData);
				iuguDao.salvar(iuguData);

				dao.salvar(mBoleto);
				setAction(Action.SHOW);
				operacaoSucesso();
			
			}else if(action.equals(Action.UPDATE)){
				if(mBoleto.getSituacao()!=MBoletoStatus.PAGAMENTO_EFETUADO)
					mBoleto.setDataRegistroPagamento(null);
				
				if(mBoleto.getSituacao()==MBoletoStatus.CANCELADO)
					iuguUtils.cancelIuguInvoice(mBoleto);
				
				dao.merge(mBoleto);
				
				
				operacaoSucesso();

			}
			boletoTable.refreshPage();
			remessaTable.refreshPage();
			return "boletoTable";
		}else
			return "boletoForm";
	}
	
	
	

	

	public String sair(){
		return "boletoTable";
	}
	
	private Boolean validaGravacao() {
		
		if (cliente == null) {
			operacaoErro("error.boleto.cliente.nao.informado","cliente",false);
			return false;
		}

		if (mBoleto.getValor() <= 1) {
			operacaoErro("error.boleto.valor.maior.que.um","valor",false);
			return false;
		}
		
		if (mBoleto.getDataVencimento() == null) {
			operacaoErro("error.boleto.vencimento.nao.informado","dataVencimento",false);
			return false;
		}
		
		if (mBoleto.getDataEmissao() == null) {
			operacaoErro("error.boleto.emissao.nao.informado","dataEmissao",false);
			return false;
		}
		
		
		if (StringUtils.isBlank(mBoleto.getMensagem34()) && action.equals(Action.INSERT)) {
			operacaoErro("error.boleto.mensagem34.null","mensagem34",false);
			return false;
		}

		
		if (mBoleto.getMensagem34() != null && mBoleto.getMensagem34().length()>80) {
			operacaoErro("error.boleto.mensagem34.maior.que.80","mensagem34",false);
			return false;
		}
		
		
		if(mBoleto.getSituacao() == MBoletoStatus.PAGAMENTO_EFETUADO && mBoleto.getDataRegistroPagamento() == null){
			operacaoErro("error.boleto.dateRegistroPagamento.null","mensagem34",false);
			return false;
		}
		
		
		if(action.equals(Action.UPDATE)){
			MBoleto doBanco = dao.findById(mBoleto.getId());
			if(doBanco.getSituacao()==MBoletoStatus.CANCELADO && mBoleto.getSituacao()!=MBoletoStatus.PAGAMENTO_EFETUADO){
				operacaoErro("error.boleto.operacao.nao.permitida","situacao",false);
				return false;
			}
			
			
			
			
		}


		
		/*
		
		if(isNumeroBoletoDuplicado(boleto.getNumero())){
			operacaoErro("error.boleto.numero.duplicado","numero",false);
			return false;
		}
		
		if(StringUtils.isNotBlank(boleto.getIccid())&& isIccidDuplicado(boleto.getIccid())){
			operacaoErro("error.boleto.iccid.duplicado","iccid",false);
			return false;
		}*/
		

		return true;
	}
	
	
/*	public void updateDataRegistroPagamento(){
		MBoleto doBanco = dao.findById(mBoleto.getId());
		if(mBoleto.getSituacao()==MBoletoStatus.PAGAMENTO_EFETUADO && doBanco.getSituacao()!=MBoletoStatus.PAGAMENTO_EFETUADO){
			mBoleto.setDataRegistroPagamento(new Date());
		}else{
			mBoleto.setDataRegistroPagamento(null);
		}
	}
*/
	
	
/*
	private boolean isNumeroBoletoDuplicado(String numero) {
		Boleto doBanco = dao.findByNumero(numero);
		return (doBanco!=null && doBanco.getId()!= boleto.getId());
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
	
	public TipoDeCobranca[] getTipoDeCobrancas(){
		return TipoDeCobranca.values();
	}


	public MBoleto getBoleto() {
		return mBoleto;
	}

	public void setBoleto(MBoleto mBoleto) {
		this.mBoleto = mBoleto;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Remessa getRemessa() {
		return remessa;
	}

	public void setRemessa(Remessa remessa) {
		this.remessa = remessa;
	}

	
	
	


}