package net.moveltrack.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import net.moveltrack.domain.Despesa;
import net.moveltrack.domain.DespesaTipo;
import net.moveltrack.domain.OrdemDeServico;

@Stateless
@SuppressWarnings("serial")
public class DespesaDao extends DaoBean<Despesa>{

	public DespesaDao() {
		
	}

	public Despesa findByOrdemDeServico(OrdemDeServico ordemDeServico) {
		Query query = getEm().createQuery("select o from Despesa o where o.osid=:ordemDeServicoId");
		query.setParameter("ordemDeServicoId",ordemDeServico.getId());
		try{
			return (Despesa)query.getSingleResult();
		}catch(Exception e){
			return null;
		}

	}
	
	public List<Despesa> getDespesaPorTipoIntervalo(DespesaTipo tipo, Date inicio, Date fim){
		
		String filtro = " P.tipoDeDespesa=:tipoDeDespesa and ";
		if(tipo==null)
			filtro= "";
		
		Query query = getEm().createQuery("select P from Despesa P where "+ filtro  +"  P.dataDaDespesa>=:inicio and P.dataDaDespesa <=:fim  order by P.dataDaDespesa");
		if(tipo!=null)
			query.setParameter("tipoDeDespesa",tipo);
		query.setParameter("inicio",inicio);
		query.setParameter("fim",fim);		
		return (List<Despesa>)query.getResultList();
	}

	public double getSomaDespesaPorTipoIntervalo(DespesaTipo tipo, Date inicio, Date fim) {
		try {
			Query query = getEm().createQuery("select SUM(P.valor) from Despesa P where P.tipoDeDespesa=:tipoDeDespesa and P.dataDaDespesa>=:inicio and P.dataDaDespesa <=:fim");
			query.setParameter("tipoDeDespesa",tipo);
			query.setParameter("inicio",inicio);
			query.setParameter("fim",fim);		
			return (Double)query.getSingleResult();
		} catch (Exception e) {
			return 0;
		}
	}

	
	
	



}
