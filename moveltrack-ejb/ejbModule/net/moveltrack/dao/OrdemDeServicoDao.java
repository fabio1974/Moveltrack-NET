package net.moveltrack.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.OrdemDeServico;

@Stateless
@SuppressWarnings("serial")
public class OrdemDeServicoDao extends DaoBean<OrdemDeServico>{

	public OrdemDeServicoDao() {
		
	}

	public OrdemDeServico findByNumero(String numero) {
		try {
			Query query = getEm().createQuery("select o from OrdemDeServico o where o.numero = :numero");
			query.setParameter("numero", numero);
			return (OrdemDeServico)query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	
	@SuppressWarnings("unchecked")
	public List<OrdemDeServico> findByInicioFimOperador(Date inicio, Date fim, Empregado operador) {
		try {
			Query query = getEm().createQuery("select o from OrdemDeServico o "
					+ "where o.dataDoServico >=:inicio and o.dataDoServico <=:fim "
					+ "and operador.id=:operadorId order by dataDoServico");
			query.setParameter("inicio",inicio);
			query.setParameter("fim",fim);
			query.setParameter("operadorId", operador.getId());
			return (List<OrdemDeServico>)query.getResultList();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	



}
