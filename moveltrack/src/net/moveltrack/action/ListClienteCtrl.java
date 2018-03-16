package net.moveltrack.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.domain.Cliente;

@Named
@SessionScoped
public class ListClienteCtrl extends BaseAction implements Serializable{

	private static final long serialVersionUID = -290830071608020288L;

	public ListClienteCtrl() { }
	
	@PersistenceContext( unitName="moveltrack-jpa-pu" , type=PersistenceContextType.EXTENDED)
	private EntityManager em;
	
	private String nome;
	private String matricula;
	private String cpf;
	

	private List<Cliente> clientes;
	
	@PostConstruct
	public void init(){
		clientes = new ArrayList<Cliente>(0);
	}
	
	@SuppressWarnings("unchecked")
	public void consultar(){
		
		String hql = "select a from Cliente a inner join a.usuario u where 0 = 0 ";
		
		if( StringUtils.isNotBlank(cpf) ){
			hql += " and u.cpf = '" + cpf.replace(".", "").replace("-", "") + "'";
		}
		
		if(StringUtils.isNotBlank(nome)){
			hql += " and upper(a.nome) like '%" + nome.toUpperCase() + "%'";
		}
		
		if(StringUtils.isNotBlank(matricula)){
			hql += " and a.matricula = '" + matricula + "'";
		}
		
		try {
			
			Query query = em.createQuery(hql);
			clientes = query.getResultList();
			
		} catch (NoResultException e) {
			operacaoNenhumRegistroEncontrado();
		} catch (Exception e) {
			operacaoErro(e.getMessage());
		}
		
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}


}
