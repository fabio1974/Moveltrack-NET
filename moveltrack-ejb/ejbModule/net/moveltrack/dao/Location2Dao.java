package net.moveltrack.dao;

import java.util.Date;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Location2;

@Stateless
@SuppressWarnings("serial")
public class Location2Dao extends DaoBean<Location2>{

	public Location2Dao() { }
	

	@SuppressWarnings("unchecked")
	public Location2 getLastLocation2FromImei(String imei) {
		String sql = "select l from Location2 l where l.imei = '"+imei+"'  order by l.dateLocation desc ";
		Query q = getEm().createQuery(sql);
		q.setMaxResults(1);
		try{
			return  (Location2)q.getResultList().get(0);
		}catch(Exception e){
			return null;
		}
	}



	public boolean existsWithThaDateAndImei(Date dateLocation, String imei) {
		Location2 l2 = null;
		String sql = "select l from Location2 l where l.imei =:imei and l.dateLocation =:dateLocation   ";
		Query q = getEm().createQuery(sql);
		q.setParameter("imei",imei);
		q.setParameter("dateLocation",dateLocation);
		q.setMaxResults(1);
		try{
			l2 =  (Location2)q.getResultList().get(0);
			return l2!=null;
		}catch(Exception e){
			return false;
		}
	}



}

