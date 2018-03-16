package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Motorista;
import net.moveltrack.domain.PessoaStatus;


@Stateless
@SuppressWarnings("serial")
public class MotoristaDao extends DaoBean<Motorista>{


	public MotoristaDao() { }

	public List<Motorista> findByTerm(String term){
		List<Motorista> list = new ArrayList<Motorista>();
		Query query = getEm().createQuery("select o from Motorista o where UPPER(o.nome) like '%"+term.toUpperCase()+"%' order by o.nome");
		try{
			list = (List<Motorista>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	


	public Motorista findByNome(String nome) {
		Query query = getEm().createQuery("select o from Motorista o where o.nome =:nome");
		query.setParameter("nome",nome);
		return (Motorista)query.getSingleResult();
	}
	
	

	
	
	
	public Motorista findByCpf(String cpf){
		Query query = getEm().createQuery("select o from Motorista o where o.cpf=:cpf");
		query.setParameter("cpf",cpf);
		try{
			return (Motorista)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	public List<Motorista> findByCliente(Cliente cliente) {
		Query query = getEm().createQuery("select o from Motorista o where o.patrao.id=:clienteId");
		query.setParameter("clienteId",cliente.getId());
		try{
			return (List<Motorista>)query.getResultList();
		}catch(Exception e){
			return null;
		}
	}
	
	public List<Motorista> findAtivosByCliente(Cliente cliente) {
		Query query = getEm().createQuery("select o from Motorista o where o.patrao.id=:clienteId and o.status=:status order by o.nome");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("status",PessoaStatus.ATIVO);
		
		try{
			return (List<Motorista>)query.getResultList();
		}catch(Exception e){
			return null;
		}
	}

	public List<Motorista> getRelatorioCNH(Cliente cliente,Date fimExpiracao) {
		Query query = getEm().createQuery("select o from Motorista o where o.patrao.id=:clienteId and o.validadeCnh<:fimExpiracao and o.status=:status order by o.validadeCnh");
		query.setParameter("clienteId",cliente.getId());
		query.setParameter("fimExpiracao",fimExpiracao);
		query.setParameter("status",PessoaStatus.ATIVO);
		try{
			return (List<Motorista>)query.getResultList();
		}catch(Exception e){
			return null;
		}
	}

	
	


}
