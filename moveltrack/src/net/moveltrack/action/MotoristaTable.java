package net.moveltrack.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.MotoristaDao;
import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.Pessoa;

@Named
public class MotoristaTable extends TableBean {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	MotoristaDao motoristaDao;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = motoristaDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;
        Pessoa pessoa = loginBean.getPessoaLogada(); 
        if(pessoa instanceof Cliente)
        	cliente = (Cliente)pessoa;
		refreshPage();        
	}
	
	List<Motorista> motoristas;
	
	public List<Motorista> getMotoristas() {
        return motoristas;
	}	
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		System.out.println("refreshing...");
		try {
			setFilters();
			motoristas = motoristaDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = motoristaDao.countAll(filters);
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
		//preFilters.put("cpf",cpf);
		preFilters.put("celular1",celular1);
		preFilters.put("endereco",endereco);
		preFilters.put("patraoId",cliente);
		
		normalizeFilters();
	}

	
	String nome;
	Cliente cliente;
	String celular1;
	String endereco;

	public MotoristaDao getMotoristaDao() {
		return motoristaDao;
	}

	public void setMotoristaDao(MotoristaDao motoristaDao) {
		this.motoristaDao = motoristaDao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

/*	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}*/



	public String getCelular1() {
		return celular1;
	}

	public void setCelular1(String celular1) {
		this.celular1 = celular1;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public void setMotoristas(List<Motorista> motoristas) {
		this.motoristas = motoristas;
	}
	

	
}
