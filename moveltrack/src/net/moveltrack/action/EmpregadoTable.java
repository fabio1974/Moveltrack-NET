package net.moveltrack.action;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.EmpregadoDao;
import net.moveltrack.dao.PessoaDao;
import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.Permission;
import net.moveltrack.domain.PessoaStatus;
import net.moveltrack.security.LoginBean;

@Named
@SessionScoped
public class EmpregadoTable extends TableBeanSession {
	
	private static final long serialVersionUID = 6187358528465253532L;

	@Inject
	EmpregadoDao empregadoDao;
	
	@PostConstruct
	public void init() {
        rowsPerPage = 5;
        sortField = "id"; 
        totalRows = empregadoDao.countAll();
        lastPage = (totalRows / rowsPerPage) + ((totalRows % rowsPerPage != 0) ? 1 : 0);
        currentPage = 1;//lastPage>0?lastPage:1;
        setFiltrosAcesso();
        //sortAscending = true;
        //filtersKeys = new String[] {"nome","cpf","municipio","celular1","endereco"};
		refreshPage();        
	}
	
	@Inject
	PessoaDao pessoaDao;
	
	@Inject
	LoginBean loginBean;
	
	private void setFiltrosAcesso() {
		if(loginBean.hasPermission(Permission.EMPREGADO_VER_PROPRIO)){
			try{
				nome = pessoaDao.findByUsuario(loginBean.getUsuario()).getNome();
			} catch (Exception e) {
				System.out.println("ERRO FILTRO!");
				e.printStackTrace();
			}
		}
	}
	
	
	List<Empregado> empregados;
	
	public List<Empregado> getEmpregados() {
        return empregados;
	}	
	
	@Override
	public void refreshPage() {
		int firstRow = (currentPage - 1) * rowsPerPage;
		System.out.println("refreshing...");
		try {
			setFilters();
			empregados = empregadoDao.listPage(firstRow>=0?firstRow:0, rowsPerPage, sortField, sortAscending, filters);
			totalRows = empregadoDao.countAll(filters);
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
		preFilters.put("cpf",cpf);
		preFilters.put("municipioId",municipio);
		preFilters.put("celular1",celular1);
		preFilters.put("endereco",endereco);
		preFilters.put("status",status);
		normalizeFilters();
		setFiltrosAcesso();
	}

	
	String nome;
	String cpf;
	String municipio;
	String celular1;
	String endereco;
	PessoaStatus status;

	public EmpregadoDao getEmpregadoDao() {
		return empregadoDao;
	}

	public void setEmpregadoDao(EmpregadoDao empregadoDao) {
		this.empregadoDao = empregadoDao;
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

	public PessoaStatus getStatus() {
		return status;
	}

	public void setStatus(PessoaStatus status) {
		this.status = status;
	}

	public String getMunicipio() {
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

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

	public void setEmpregados(List<Empregado> empregados) {
		this.empregados = empregados;
	}
	

	
}
