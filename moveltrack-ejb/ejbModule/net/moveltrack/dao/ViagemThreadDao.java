package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.persistence.Query;

import net.moveltrack.domain.Viagem;
import net.moveltrack.domain.ViagemStatus;


@Stateful
@SuppressWarnings("serial")
public class ViagemThreadDao extends DaoBean<Viagem>{


	public ViagemThreadDao() { }
	
	@PostConstruct
	public void  init(){
		System.out.println("...Init on "+this.getClass().getName());
	}

	public List<Viagem> findNaoEncerradas() {
		List<Viagem> list = new ArrayList<Viagem>();
		String sql = "select o from Viagem o where "
					+" o.partida <:now and o.status<>:status  ";
 
		
		Query query = getEm().createQuery(sql);
		query.setParameter("now",new Date());
		query.setParameter("status",ViagemStatus.ENCERRADA);
		try{
			list = (List<Viagem>)query.getResultList();
		}catch(Exception e){
			return list;
		}
		return list;
	}
	
	
	public List<Viagem> findAllJunho() {
		List<Viagem> list = new ArrayList<Viagem>();
		String sql = "select o from Viagem o where "
					+ " o.partida >= '2016-06-01 00:00'"
					+ " and o.partida <= '2016-06-30 23:59'";
		
		Query query = getEm().createQuery(sql);
		try{
			list = (List<Viagem>)query.getResultList();
		}catch(Exception e){
			return list;
		}
		return list;
	}

	
	
	public List<Viagem> findEncerradas() {
		List<Viagem> list = new ArrayList<Viagem>();
		String sql = "select o from Viagem o where "
					+" o.partida <:now and o.status=:status  ";
 
		
		Query query = getEm().createQuery(sql);
		query.setParameter("now",new Date());
		query.setParameter("status",ViagemStatus.ENCERRADA);
		try{
			list = (List<Viagem>)query.getResultList();
		}catch(Exception e){
			return list;
		}
		return list;
	}
	
	
	

	




}
