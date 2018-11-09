package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Equipamento;
import net.moveltrack.domain.ModeloRastreador;

@Stateless
@SuppressWarnings("serial")
public class EquipamentoDao extends DaoBean<Equipamento>{


	
	public List<Equipamento> findByTerm(String term){
		List<Equipamento> list = new ArrayList<Equipamento>();
		Query query = getEm().createQuery("select o from Equipamento o where imei like '%"+term+"%'  order by o.imei");
		try{
			list = (List<Equipamento>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	
	public Equipamento findByImei(String imei) {
		String hql = "select  o from Equipamento o where o.imei = :imei";
		try {
			Query query = getEm().createQuery(hql);
			query.setParameter("imei",imei);
			return (Equipamento)query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Equipamento> findByModelo(ModeloRastreador modelo) {
		String hql = "select  o from Equipamento o where o.modelo = :modelo order by o.imei" ;
		try {
			Query query = getEm().createQuery(hql);
			query.setParameter("modelo",modelo);
			return (List<Equipamento>)query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	public Equipamento findByChipIccid(String iccid) {
		String hql = "select  o from Equipamento o where o.chip.iccid = :iccid";
		try {
			Query query = getEm().createQuery(hql);
			query.setParameter("iccid",iccid);
			return (Equipamento)query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}



}
