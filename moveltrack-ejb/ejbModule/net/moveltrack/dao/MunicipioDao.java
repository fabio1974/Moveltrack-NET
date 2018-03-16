package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Municipio;
import net.moveltrack.domain.Uf;

@Stateless
@SuppressWarnings("serial")
public class MunicipioDao extends DaoBean<Municipio>{


	public MunicipioDao() { }

	
	public List<Municipio> findByTerm(String term){
		List<Municipio> list = new ArrayList<Municipio>();
		Query query = getEm().createQuery("select o from Municipio o where UPPER(o.descricao) like '"+term.toUpperCase()+"%' order by o.descricao");
		try{
			list = (List<Municipio>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	
	public Municipio findByDescricaoUf(String descricao,String ufSigla){
		Query query = getEm().createQuery("select o from Municipio o where UPPER(o.descricao) =:descricao and UPPER(o.uf.sigla)=:ufSigla");
		query.setParameter("descricao",descricao.toUpperCase());
		query.setParameter("ufSigla",ufSigla.toUpperCase());
		try{
			return  (Municipio)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}

	
	
	public List<Municipio> findByUf(Uf uf){
		List<Municipio> list = new ArrayList<Municipio>();
		Query query = getEm().createQuery("select o from Municipio o where o.uf.id=:id order by o.descricao");
		query.setParameter("id",uf.getId());
		try{
			list = (List<Municipio>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}



	
	



}
