package net.moveltrack.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Uf;

@Stateless
@SuppressWarnings("serial")
public class UfDao extends DaoBean<Uf>{

	public UfDao() { }
	
	
	public List<Uf> buscaUfPorOrgao(){
		
		String hql = "select o from Uf o";
		
		Query query = getEm().createQuery(hql);
		List<Uf> ufs = query.getResultList();
		System.out.println(ufs);
		return ufs;
		
	}
	
	

}
