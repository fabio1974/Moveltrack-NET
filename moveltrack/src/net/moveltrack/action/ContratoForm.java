package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.dao.VeiculoDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.ContratoGeraCarne;
import net.moveltrack.domain.ContratoStatus;
import net.moveltrack.domain.ContratoTipo;
import net.moveltrack.domain.PessoaStatus;
import net.moveltrack.domain.Usuario;


@Named
@SessionScoped
public class ContratoForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	private Conversation conversation;



	public ContratoForm() {
		System.out.println("Constructor ... ");
	}

	private Contrato contrato;
	
	private String action = Action.INSERT; 

	//private List<Cliente> clientes;
	//private List<Pessoa> vendedores;
	
	
	@Inject
	ClienteDao clienteDao;
	
	@Inject
	PessoaDao pessoaDao;
	
	private Cliente clienteSelecionado;


	@Inject
	private ContratoDao contratoDao;
	
	@PostConstruct
	public void init() {
		System.out.println("Init ... ");
		if(contrato==null)
			buildNewObject();
		//clientes = clienteDao.findAll();
		//vendedores = pessoaDao.findAll();
	}
	
	private void buildNewObject(){
		contrato = new Contrato();
		contrato.setInicio(new Date());
		contrato.setContratoTipo(ContratoTipo.COMODATO);
	}
	

	
	@Transactional
	public String salvar() {
		if (validaGravacao()) {

			contrato.setUltimaAlteracao(new Date());
			
			
			if(action.contains(Action.INSERT)){
				contrato.setContratoGeraCarne(ContratoGeraCarne.PENDENTE);
				contratoDao.salvar(contrato);
				contrato.setNumeroContrato(String.format("%07d",100000+contrato.getId()));
				contratoDao.merge(contrato);
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				contrato.setNumeroContrato(String.format("%07d",100000+contrato.getId()));
				
				Cliente cliente = contrato.getCliente();

				Usuario usuario = cliente.getUsuario();
				usuario.setAtivo(contrato.getStatus()==ContratoStatus.ATIVO);
				usuarioDao.merge(usuario);

				cliente.setStatus(contrato.getStatus()==ContratoStatus.ATIVO?PessoaStatus.ATIVO:PessoaStatus.INATIVO);
				clienteDao.merge(cliente);
				
				contratoDao.merge(contrato);
				operacaoSucesso();
			}
			contratoTable.refreshPage();
			return "contratoTable";
		}else
			return "contratoForm";
	}
	
	@Inject	ContratoTable contratoTable;
	@Inject UsuarioDao usuarioDao;


	public String sair(){
		//endConversation();
		return "contratoTable";
	}

	
	private boolean clienteJaTemContratoAtivo(Cliente cliente) {
		Contrato doBanco = contratoDao.findByClienteStatus(cliente,ContratoStatus.ATIVO);
		if(action.equals(Action.INSERT) || action.equals(Action.SMARTINSERT))
			return doBanco!=null;
		else if(action.equals(Action.UPDATE))
			return (doBanco!=null && doBanco.getId().intValue()!= contrato.getId().intValue());
		else
			return false;
	}


	private Boolean validaGravacao() {
		if (contrato.getCliente() == null || contrato.getCliente().getId() == null) {
			operacaoErro("error.contrato.cliente.nao.informado","cliente",false);
			return false;
		}
		
		if (contrato!=null && clienteJaTemContratoAtivo(contrato.getCliente())) {
			operacaoErro("error.contrato.cliente.jaTemContratoAtivo","cliente", false);
			return false;
		}		
		
		if (contrato.getDiaVencimento()<=0) {
			operacaoErro("error.contrato.diaVencimento.nao.informado","diaVencimento", false);
			return false;
		}		
	
		if (contrato.getInicio()==null) {
			operacaoErro("error.contrato.inicio.nao.informado","inicio", false);
			return false;
		}
		
		if (contrato.getContratoTipo()==null) {
			operacaoErro("error.contrato.tipo.nao.informado","contratoTipo", false);
			return false;
		}
		
		if (contrato.getStatus()==null) {
			operacaoErro("error.contrato.status.nao.informado","status", false);
			return false;
		}
		
		if(contrato.getStatus()!=ContratoStatus.ATIVO && veiculoDao.findAtivosByCliente(contrato.getCliente()).size()>0){
			operacaoErro("error.contrato.naoAtivo.com.veiculoAtivo","status", false);
			return false;
		}
		
		if (contrato.getMensalidade()<=0) {
			operacaoErro("error.contrato.mensalidade.nao.informado","mensalidade", false);
			return false;
		}
		
		if (contrato.getVendedor()==null) {
			operacaoErro("error.contrato.vendedor.nao.informado","vendedor", false);
			return false;
		}
		

		
		return true;
	}
	

	@Inject VeiculoDao veiculoDao;
	
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewObject();
		else if(action.equals(Action.SMARTINSERT)){
			Cliente cliente = contrato.getCliente();
			buildNewObject();
			contrato.setCliente(cliente);
		}		
		this.action = action;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public Contrato getContrato() {
		return contrato;
	}

	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

/*	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}*/

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
		if(clienteSelecionado!=null)
			contrato.setCliente(clienteSelecionado);
	}
	
	public ContratoTipo[] getTipos(){
		return ContratoTipo.values();
	}
	
	public ContratoStatus[] getStatuses(){
		return ContratoStatus.values();
	}

/*	public List<Pessoa> getVendedores() {
		return vendedores;
	}

	public void setVendedores(List<Pessoa> vendedores) {
		this.vendedores = vendedores;
	}
*/


}