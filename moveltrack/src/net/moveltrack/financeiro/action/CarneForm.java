package net.moveltrack.financeiro.action;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;

import net.moveltrack.action.Action;
import net.moveltrack.action.ContratoTable;
import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.CarneConteudoDao;
import net.moveltrack.dao.CarneDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.MBoletoDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Carne;
import net.moveltrack.domain.CarneConteudo;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.ContratoGeraCarne;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.Operadora;
import net.moveltrack.domain.TipoDeCobranca;
import net.moveltrack.domain.Veiculo;
import net.moveltrack.security.LoginBean;

@Named
@SessionScoped
public class CarneForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	private Carne carne;
	private CarneConteudo carneConteudo; 
	private Cliente cliente;
	private double mensalidade;
	private List<Veiculo> veiculos;

	@Inject	CarneDao carneDao;
	@Inject LoginBean loginBean;
	@Inject CarneTable carneTable;
	@Inject BoletoTable boletoTable;
	@Inject RemessaTable remessaTable;
	@Inject ContratoTable contratoTable;	
	@Inject MBoletoDao mBoletoDao;
	@Inject ContratoDao contratoDao;
	@Inject CarneConteudoDao carneConteudoDao;
	@Inject BoletoUtils boletoUtils;

	private String action = Action.INSERT; 

	public CarneForm() {
		System.out.println("carne form constructor ... ");
	}

	@PostConstruct
	public void init() {
		System.out.println("carne form  Init ... ");
		if(carne==null)
			buildNewObject();
	}
	
	private void buildNewObject(){
		carne = new Carne();
		carne.setQuantidadeBoleto(12);
		carne.setDataEmissao(new Date());
		carneConteudo = new CarneConteudo();
		carne.setConteudo(carneConteudo);
		carne.setTipoDeCobranca(TipoDeCobranca.COM_REGISTRO);
	}
	
	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {
			carne.setContrato(contratoDao.findByCliente(cliente));			
			if(action.equals(Action.INSERT)){

				carneConteudo = carne.getConteudo();
				carne.setDataVencimentoInicio(carneDao.getPrimeiroVencimento(carne.getContrato()));
				carne.getContrato().setMensalidade(mensalidade);

				carneConteudoDao.salvar(carneConteudo);
				carneDao.salvar(carne);
				carneDao.flush();
				
				//ServletContext sc = (ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
				
				Date ultimoVencimento = carneDao.geraBoletos(carne,(Empregado)loginBean.getPessoaLogada());
				carne.setDataVencimentoFim(ultimoVencimento);
				
				//carneConteudo.setConteudo(boletoUtils.geraCarne(carne,sc));
				
				carne.getContrato().setContratoGeraCarne(ContratoGeraCarne.GERADO);
				
				//carneConteudoDao.merge(carneConteudo);
				
				carneDao.merge(carne);

				setAction(Action.SHOW);
				operacaoSucesso();
				
			}else if(action.equals(Action.UPDATE)){
				carneDao.merge(carne);
				operacaoSucesso();
			}
			
			contratoTable.refreshPage();
			boletoTable.refreshPage();
			carneTable.refreshPage();
			remessaTable.refreshPage();
			
			return "carneTable";
		}else
			return "carneForm";
	}
	


	
	

	public String sair(){
		return "carneTable";
	}
	
	@Inject VeiculoDao veiculoDao;
	
	private Boolean validaGravacao() {
		if (cliente == null) {
			operacaoErro("error.carne.cliente.nao.informado","cliente",false);
			return false;
		}
		
		
		if(veiculos==null || veiculos.size()<=0){
			operacaoErro("error.carne.sem.veiculo","numero",false);
			return false;
		}
		
		return true;
	}
	

	public List<Veiculo> getVeiculos(){
		//if(veiculos==null && cliente!=null)
	 	veiculos = veiculoDao.findAtivosByCliente(cliente);
		return veiculos;
	}
	
	
	
	public String getAction() {
		return action;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT)){
			buildNewObject();
			carne.setContrato(contratoDao.findByCliente(cliente));
			carne.setDataReferencia(carneDao.getDataBase(carne.getContrato()));
		}this.action = action;
	}

	

	public Operadora[] getOperadoras(){
		return Operadora.values();
	}

	public Carne getCarne() {
		return carne;
	}

	public void setCarne(Carne carne) {
		this.carne = carne;
	}

	public double getMensalidade() {
		return mensalidade;
	}

	public void setMensalidade(double mensalidade) {
		this.mensalidade = mensalidade;
	}

	public CarneConteudo getCarneConteudo() {
		return carneConteudo;
	}

	public void setCarneConteudo(CarneConteudo carneConteudo) {
		this.carneConteudo = carneConteudo;
	}
	
	
}