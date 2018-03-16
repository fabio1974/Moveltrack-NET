package net.moveltrack.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.Query;

import net.moveltrack.domain.DespesaFrota;
import net.moveltrack.domain.Viagem;

@Stateless
@SuppressWarnings("serial")
public class ViagemDao extends DaoBean<Viagem>{


	public ViagemDao() { }
	
	@PostConstruct
	public void  init(){
		
	}


	@SuppressWarnings("unchecked")
	public List<Viagem> findByNumero(String term){
		List<Viagem> list = new ArrayList<Viagem>();
		Query query = getEm().createQuery("select o from Viagem o where UPPER(o.numeroViagem) like '%"+term.toUpperCase()+"%' order by o.numeroViagem");
		try{
			list = (List<Viagem>)query.getResultList();
		}catch(Exception e){
			return null;
		}
		return list;
	}
	
	public Viagem findViagemByNumero(int numeroViagem){
		int m = 0;
		m++;
		try{
			return (Viagem)getEm().createQuery("select o from Viagem o where o.numeroViagem ="+numeroViagem).getSingleResult();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Inject
	DespesaFrotaDao despesaFrotaDao;

	@Override
	public void remover(Viagem viagem){
		despesaFrotaDao.removeByViagem(viagem);
		remover(viagem.getId());
	}
	
	

	
	public Viagem findByDescricao(String descricao) {
		Query query = getEm().createQuery("select o from Viagem o where o.descricao=:descricao");
		query.setParameter("descricao",descricao);
		try{
			return (Viagem)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}




	public Viagem findByRui(String placa,int ano,int mes, int dia, int hora) {
		
		Viagem viagem = null;
		Calendar data = Calendar.getInstance();
		data.set(ano,mes-1,dia,hora,0);
		
		String sql = "select o from Viagem o where o.veiculo.placa=:placa and o.partida <=:data and o.chegadaReal>=:data";
		Query query = getEm().createQuery(sql);
		query.setParameter("placa",placa);
		query.setParameter("data",data.getTime());
		try{
			viagem = (Viagem)query.getSingleResult();
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		return viagem;
	}

}
