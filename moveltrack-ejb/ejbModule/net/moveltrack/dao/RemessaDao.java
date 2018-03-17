package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.transaction.Transactional;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.Remessa;





@Stateless
@SuppressWarnings("serial")
public class RemessaDao extends DaoBean<Remessa>{


	public RemessaDao() { }


	public List<Cliente> findByTerm(String term){
		List<Cliente> list = new ArrayList<Cliente>();
		Query query = getEm().createQuery("select o from Cliente o where UPPER(o.nome) like '%"+term.toUpperCase()+"%' order by o.nome");
		try{
			list = (List<Cliente>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}


	

	@Transactional
	public Remessa getRemessaAtual() {
		Remessa remessa = null;
		Query query = getEm().createQuery("select o from Remessa o where o.fechamento is null order by o.id desc");
		query.setMaxResults(1);
		try{
			remessa = (Remessa)query.getSingleResult();
		}catch(Exception e){
			Remessa aux = new Remessa();
			aux.setAbertura(new Date());
			aux.setNumero(getProximoNumero());
			getEm().persist(aux);
			return getRemessaAtual();
		}
		return remessa;
	}


	private String getProximoNumero() {
		int maxId = 0;
		Query query = getEm().createQuery("select max(o.id) from Remessa o");
		try{
			maxId = (Integer)query.getSingleResult();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		return String.format("%06d",maxId + 1) ;
	}
	
	


}
