package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Chip;

@Stateless
@SuppressWarnings("serial")
public class ChipDao extends DaoBean<Chip>{

	public ChipDao() {
		
	}
	
	

	public List<Chip> findByTerm(String term){
		List<Chip> list = new ArrayList<Chip>();
		Query query = getEm().createQuery("select o from Chip o where o.iccid like '%"+term+"%' order by o.iccid");
		try{
			list = (List<Chip>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}

	public Chip findByNumero(String numero) {
		Query query = getEm().createQuery("select o from Chip o where o.numero =:numero");
		query.setParameter("numero",numero);
		try{
			return (Chip)query.getSingleResult();	
		}catch(Exception e){
			return null;
		}
	}
	
	public Chip findByIccid(String iccid) {
		Query query = getEm().createQuery("select o from Chip o where o.iccid =:iccid");
		query.setParameter("iccid",iccid);
		try{
			return (Chip)query.getSingleResult();	
		}catch(Exception e){
			return null;
		}
	}
	
	
	public Chip findByIccidOuNumero(String iccid, String numero) {
		String sql = "select o from Chip o where o.iccid =:iccid or o.numero =:numero";
		if(iccid == null)
			sql = "select o from Chip o where o.numero =:numero";
		Query query = getEm().createQuery(sql);
		if(iccid != null)
			query.setParameter("iccid",iccid);
		query.setParameter("numero",numero);
		try{
			return (Chip)query.getSingleResult();	
		}catch(Exception e){
			return null;
		}
	}



}
