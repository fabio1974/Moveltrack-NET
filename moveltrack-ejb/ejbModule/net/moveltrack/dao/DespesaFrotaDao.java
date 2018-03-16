package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.DespesaFrota;
import net.moveltrack.domain.Viagem;


@Stateless
@SuppressWarnings("serial")
public class DespesaFrotaDao extends DaoBean<DespesaFrota>{


	public DespesaFrotaDao() { }

	public List<DespesaFrota> findByTerm(String term){
		List<DespesaFrota> list = new ArrayList<DespesaFrota>();
		Query query = getEm().createQuery("select o from DespesaFrota o where UPPER(o.nome) like '%"+term.toUpperCase()+"%' order by o.nome");
		try{
			list = (List<DespesaFrota>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	
	public List<DespesaFrota> findByViagem(Viagem viagem){
		List<DespesaFrota> list = new ArrayList<DespesaFrota>();
		Query query = getEm().createQuery("select o from DespesaFrota o where o.viagem.id = "+ viagem.getId());
		try{
			list = (List<DespesaFrota>)query.getResultList();
		}catch(Exception e){
		}
		return list;
	}
	
	
	public void removeByViagem(Viagem viagem){
		Query query = getEm().createQuery("delete from DespesaFrota o where o.viagem.id = "+ viagem.getId());
		try{
			query.executeUpdate();
		}catch(Exception e){
		}
	}


	
	


	public DespesaFrota findByNome(String nome) {
		Query query = getEm().createQuery("select o from DespesaFrota o where o.nome =:nome");
		query.setParameter("nome",nome);
		return (DespesaFrota)query.getSingleResult();
	}
	
	
	public DespesaFrota findLastByCliente(Cliente cliente) {
		Query query = getEm().createQuery("select o from DespesaFrota o where o.cliente.id =:clienteId order by o.id desc");
		query.setParameter("clienteId",cliente.getId());
		query.setMaxResults(1);
		try{
			return (DespesaFrota)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	
	

	
	
	


}
