package com.moveltrack.reactbackend.rest.api.repository.st500;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.moveltrack.reactbackend.model.Veiculo;
import com.moveltrack.reactbackend.model.st500.Location;
import com.moveltrack.reactbackend.service.Util;





public class LocationRepImpl implements LocationRepCustom {
	
	@PersistenceContext
    EntityManager entityManager;


	@Override
	public List<Object> findLocationsByVeiculoInicioFim(Veiculo veiculo, Date inicio, Date fim) {

		String orderby = "";
			orderby = " order by l.dateLocation ";

		boolean isHistorico = Util.isHistorico(inicio);
		
		String sql = "select l from "+(isHistorico?"Location":"Location2")+" l where l.imei =:imei and "+
				"("+
				"(l.dateLocation >=:inicio and l.dateLocation <=:fim)"+
				"or"+
				"(l.dateLocationInicio >=:inicio and l.dateLocationInicio <=:fim)"+
				"or"+
				"(l.dateLocationInicio <=:inicio and l.dateLocation >=:fim)"+
				")" + orderby;					
		
		Query q = entityManager.createQuery(sql);
		q.setParameter("imei",veiculo.getEquipamento().getImei());
		q.setParameter("inicio",inicio);
		q.setParameter("fim",fim);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		//System.out.println(sdf.format(fim));
		
		List<Object> result = q.getResultList(); 
		
		return result;

	}
	
	
	public Location getLastLocationFromVeiculo(Veiculo veiculo) {
		String sql = "select l from Location l where l.imei = '"+veiculo.getEquipamento().getImei()+"' and l.dateLocation<=:now order by l.dateLocation desc ";
		Query q = entityManager.createQuery(sql);
		q.setParameter("now",new Date());
		q.setMaxResults(1);
		try{
			return  (Location)q.getResultList().get(0);
		}catch(Exception e){
			return null;
		}
	}
	
	
	
}
