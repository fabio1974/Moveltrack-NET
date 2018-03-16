package net.moveltrack.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ClienteDao;
import net.moveltrack.dao.ContratoDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Contrato;
import net.moveltrack.domain.Municipio;
import net.moveltrack.domain.Permission;
import net.moveltrack.security.LoginBean;

@Named
@SessionScoped
public class ClienteTable extends TableBeanSession {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	ClienteDao clienteDao;
	
	@Inject
	PessoaDao pessoaDao;
	
	@Inject 
	LoginBean loginBean;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = clienteDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;//lastPage>0?lastPage:1;
        setFiltrosAcesso();
        refreshPage();
	}
	
	private void setFiltrosAcesso() {
		if(loginBean.hasPermission(Permission.CLIENTE_VER_PROPRIO)){
			try{
				nome = pessoaDao.findByUsuario(loginBean.getUsuario()).getNome();
			} catch (Exception e) {
				System.out.println("ERRO FILTRO!");
				e.printStackTrace();
			}
		}
	}
	
	
	List<Cliente> clientes;
	
	public List<Cliente> getClientes() {
        return clientes;
	}	
	
	
	/*public List<Cliente> getClients() {
		return clienteDao.findAll();
	}*/
	
	
	public String smartAction(Cliente cliente){
		return cliente.getContrato()!=null?"contratoTable":"contratoForm";
	}
	
	
	@Inject
	ContratoDao contratoDao;
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		System.out.println("refreshing...");
		try {
			setFilters();
			clientes = clienteDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			
			for (Cliente cliente : clientes) {
				Contrato contrato = contratoDao.findByClienteId(cliente.getId());
				cliente.setContrato(contrato);
			}
			
			
			totalRows = clienteDao.countAll(filters);
		} catch (Exception e) {
			throw new RuntimeException(e); // Handle it yourself.
		}
		lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage==0)
			currentPage=1;
	}
	
	

	@Override
	public void setFilters() {
		preFilters.put("nome",nome);
		preFilters.put("nomeFantasia",nomeFantasia);
		preFilters.put("cpf",cpf);
		preFilters.put("bairro",bairro);
		preFilters.put("municipioId",municipio);
		preFilters.put("celular1",celular1);
		preFilters.put("endereco",endereco);
		normalizeFilters();
		setFiltrosAcesso();
	}
	
	
	
	String nome;
	String nomeFantasia;
	String cpf;
	String bairro;
	Municipio municipio;
	String celular1;
	String endereco;

	

	
	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


	public String getBairro() {
		return bairro;
	}


	public void setBairro(String bairro) {
		this.bairro = bairro;
	}





	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	public String getCelular1() {
		return celular1;
	}


	public void setCelular1(String celular1) {
		this.celular1 = celular1;
	}


	public ClienteDao getClienteDao() {
		return clienteDao;
	}


	public void setClienteDao(ClienteDao clienteDao) {
		this.clienteDao = clienteDao;
	}


	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}


	public String getEndereco() {
		return endereco;
	}


	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	
}
