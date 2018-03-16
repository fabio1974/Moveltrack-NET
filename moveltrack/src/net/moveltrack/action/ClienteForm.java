package net.moveltrack.action;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.dao.CercaDao;
import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.PerfilDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.dao.UsuarioDao;
import net.moveltrack.domain.Cerca;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.PerfilTipo;
import net.moveltrack.domain.Pessoa;
import net.moveltrack.domain.PessoaStatus;
import net.moveltrack.domain.Usuario;
import net.moveltrack.util.Criptografia;
import net.moveltrack.util.Utils;
import net.moveltrack.util.ValidaCnpj;
import net.moveltrack.util.ValidaCpf;


@Named
@SessionScoped
public class ClienteForm extends BaseAction implements Serializable {

	private static final long serialVersionUID = 402618051540830394L;

	@Inject
	private Conversation conversation;

	private Boolean valido;

	public ClienteForm() {
		System.out.println("addAgenteCtrl->Constructor ... ");
	}

	private Cliente cliente;
	private PerfilTipo tipo;
	
	private String confirmaSenha1;
	private String confirmaSenha2;
	private String action = Action.INSERT; 



	@Inject
	PerfilDao perfilDao;

	
	@PostConstruct
	public void init() {
		System.out.println("Init() from..."+this.getClass().getName());
		if(cliente==null)
			buildNewCliente();
	}
	
	private void buildNewCliente(){
		cliente = new Cliente();
		Cerca cerca = buildNewCerca();
		Usuario usuario = new Usuario();
		try {
			usuario.setSenha(new Criptografia().calculaHash("1234"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		usuario.setAtivo(Boolean.TRUE);
		usuario.setPerfil(perfilDao.findByTipo(PerfilTipo.CLIENTE_PF));
		cliente.setUsuario(usuario);
		cliente.setCerca(cerca);
		cliente.setStatus(PessoaStatus.ATIVO);
		setTipo(PerfilTipo.CLIENTE_PF);
		cliente.setDataCadastro(new Date());
	}

	@Inject
	CercaDao cercaDao;
	
	@Transactional
	public String save() {
		if (validaGravacao()) {
			
			cliente.setUltimaAlteracao(new Date());
			cliente.getUsuario().setPerfil(perfilDao.findByTipo(tipo));
			cliente.getUsuario().setAtivo(cliente.getStatus()==PessoaStatus.ATIVO);
			
			if(tipo==PerfilTipo.CLIENTE_PF){
				cliente.setCnpj(null);
			}else if (tipo ==PerfilTipo.CLIENTE_PJ){
				cliente.setCpf(null);
			}	
			
			if(action.equals(Action.INSERT)){
				usuarioDao.salvar(cliente.getUsuario());
				clienteDao.salvar(cliente);
				cercaDao.salvar(cliente.getCerca());
				setAction(Action.SHOW);
				operacaoSucesso();
			}else if(action.equals(Action.UPDATE)){
				usuarioDao.merge(cliente.getUsuario());
				if(cliente.getCerca()==null || cliente.getCerca().getId()==null){
					cliente.setCerca(buildNewCerca());
					cercaDao.salvar(cliente.getCerca());
				}	
				cercaDao.merge(cliente.getCerca());
				clienteDao.merge(cliente);
				operacaoSucesso();
			}			
			clienteTable.refreshPage();
			return "clienteTable";
		}else
			return "clienteForm";
	}
	
	
	
	@Transactional
	public String saveCerca() {
		cliente.setUltimaAlteracao(new Date());
		if(cliente.getCerca()==null || cliente.getCerca().getId()==null){
			cliente.setCerca(buildNewCerca());
			cercaDao.salvar(cliente.getCerca());
		}
		cercaDao.merge(cliente.getCerca());
		clienteDao.merge(cliente);
		operacaoSucesso();
		return "cercaTemplate";
	}
	
	
	
	private Boolean validaSalvaSenha(){ 
		
		if(StringUtils.isBlank(confirmaSenha1)){
			operacaoErro("error.senhaConfirma.nula","confirmaSenha1",false);
			return false;
		}
		
		if(!confirmaSenha1.equals(confirmaSenha2)){
			operacaoErro("error.cliente.senhaConfirma.invalido","confirmaSenha2",false);
			return false;
		}
		return true;
	}
	
	
	
	@Transactional
	public String salvaSenha() {
		if(validaSalvaSenha()){
			cliente.getUsuario().setSenha(new Criptografia().calculaHash(confirmaSenha1));
			usuarioDao.merge(cliente.getUsuario());
			operacaoSucesso();
		}
		return "clienteSenha";
	}
	

	
	
	
	
	@Inject
	ClienteTable clienteTable;

	

	private Cerca buildNewCerca(){
		Cerca cerca = new Cerca();
		cerca.setLat1(-13.34);
		cerca.setLon1(-51.82);
		cerca.setRaio(300);
		return cerca;
	}
	

	
	
	private Boolean validaGravacao() {

		if (cliente == null) {
			operacaoErro("error.cliente.null");
			return false;
		}
		
		if (StringUtils.isEmpty(cliente.getNome())) {
			operacaoErro("error.cliente.nome.nao.informado","nome", false);
			return false;
		}
		

		if(tipo==PerfilTipo.CLIENTE_PJ){
			if (StringUtils.isEmpty(cliente.getCnpj())) {
				operacaoErro("error.cliente.cnpj.nao.informado","cnpj",false);
				return false;

			} else if (!ValidaCnpj.validaCnpj(cliente.getCnpj())) {
				operacaoErro("error.cliente.cnpj.invalido","cnpj",false);
				return false;
			} else if(isCnpjDuplicado(cliente.getCnpj())){
				operacaoErro("error.cliente.cnpj.duplicado","cnpj",false);
				return false;
			}
			
			if(StringUtils.isBlank(cliente.getNomeFantasia())){
				operacaoErro("error.cliente.cnpj.nomefantasia.nao.informado","nomeFantasia",false);
				return false;
				
			}
			
		}else{
			if (StringUtils.isEmpty(cliente.getCpf())) {
				operacaoErro("error.cliente.cpf.nao.informado","cpf", false);
				return false;
			} else if (!ValidaCpf.isValido(cliente.getCpf())) {
				operacaoErro("error.cliente.cpf.invalido","cpf", false);
				return false;
			}else if(isCpfDuplicado(cliente.getCpf())){
				operacaoErro("error.cliente.cpf.duplicado","cpf",false);
				return false;
			}
		}
		

		if(!StringUtils.isEmpty(cliente.getEmail()) && !Utils.validEmail(cliente.getEmail())){
			operacaoErro("error.cliente.email.invalido","email", false);
			return false;
		}
		
		if(!StringUtils.isEmpty(cliente.getEmailAlarme()) && !Utils.validEmail(cliente.getEmailAlarme())){
			operacaoErro("error.cliente.emailAlarme.invalido","emailAlarme", false);
			return false;
		}

				
				
		
		if (StringUtils.isEmpty(cliente.getEndereco())) {
			operacaoErro("error.cliente.endereco.nao.informado","endereco", false);
			return false;
		}		

		if (StringUtils.isEmpty(cliente.getBairro())) {
			operacaoErro("error.cliente.bairro.nao.informado","bairro", false);
			return false;
		}		

		if (cliente.getMunicipio()==null) {
			operacaoErro("error.cliente.municipio.nao.informado","municipio", false);
			return false;
		}		
		

		if (StringUtils.isEmpty(cliente.getCep())) {
			operacaoErro("error.cliente.cep.nao.informado","cep", false);
			return false;
		}		
		
		if (StringUtils.isEmpty(cliente.getUsuario().getNomeUsuario())) {
			operacaoErro("error.usuario.nomeUsuario.nao.informado","nomeUsuario", false);
			return false;
		}	
		
		
		if(isUsuarioInternetDuplicado(cliente.getUsuario().getNomeUsuario())){
			operacaoErro("error.usuario.nomeUsuario.duplicado","nomeUsuario", false);
			return false;
		}
		
	
		if (StringUtils.isEmpty(cliente.getCelular1())) {
			operacaoErro("error.cliente.celular1.nao.informado","celular1", false);
			return false;
		}
		
		if (StringUtils.isEmpty(cliente.getCelular2())) {
			operacaoErro("error.cliente.celular2.nao.informado","celular2", false);
			return false;
		}
		
		
		if (cliente.getStatus()==null){
			operacaoErro("error.cliente.status.nao.informado","status", false);
			return false;
		}
		
		

		return true;
	}

	
	@Inject
	ClienteDao clienteDao;
	
	@Inject
	PessoaDao pessoaDao;

	
	@Inject
	UsuarioDao usuarioDao;
	
	private boolean isCpfDuplicado(String cpf) {
		Pessoa doBanco = pessoaDao.findByCpf(cpf);
		if(action.equals(Action.INSERT) || action.equals(Action.SMARTINSERT))
			return doBanco!=null;
			else if(action.equals(Action.UPDATE))	
					return doBanco!=null && doBanco.getId().intValue()!= cliente.getId().intValue();
			else return false;
	}
	

	private boolean isCnpjDuplicado(String cnpj) {
		Pessoa doBanco = pessoaDao.findByCnpj(cnpj);
		if(action.equals(Action.INSERT) || action.equals(Action.SMARTINSERT))
			return doBanco!=null;
			else if(action.equals(Action.UPDATE))	
					return doBanco!=null && doBanco.getId().intValue()!= cliente.getId().intValue();
			else return false;
	}
	
	private boolean isUsuarioInternetDuplicado(String nomeUsuario) {
		Usuario doBanco = usuarioDao.findByNomeUsuario(nomeUsuario);
		if(action.equals(Action.INSERT) || action.equals(Action.SMARTINSERT))
			return doBanco!=null;
			else if(action.equals(Action.UPDATE))	
					return doBanco!=null && doBanco.getId().intValue()!= cliente.getUsuario().getId().intValue();
			else return false;
	}
	

	public String sair(){
		//endConversation();
		return "clienteTable";
	}
	
	/*public void updateFields(){
		System.out.println(cliente.getUsuario().getPerfil().getTipo());
	}*/
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		if(action.equals(Action.INSERT))
			buildNewCliente();
		this.action = action;
	}

	public Conversation getConversation() {
		return conversation;
	}

	public void setConversation(Conversation conversation) {
		this.conversation = conversation;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		if(cliente.getCerca()==null)
			cliente.setCerca(buildNewCerca());
		this.cliente = cliente;
	}


	public String getConfirmaSenha1() {
		return confirmaSenha1;
	}

	public void setConfirmaSenha1(String confirmaSenha1) {
		this.confirmaSenha1 = confirmaSenha1;
	}

	public String getConfirmaSenha2() {
		return confirmaSenha2;
	}

	public void setConfirmaSenha2(String confirmaSenha2) {
		this.confirmaSenha2 = confirmaSenha2;
	}

	public PerfilTipo[] getClienteTipos(){

		return  new PerfilTipo[]{
				PerfilTipo.CLIENTE_PF,
				PerfilTipo.CLIENTE_PJ
		};
	}

	public PerfilTipo getTipo() {
		return tipo;
	}

	public void setTipo(PerfilTipo tipo) {
		this.tipo = tipo;
	}
	
	
	public PessoaStatus[] getStatuses(){
		return PessoaStatus.values();
	}

}