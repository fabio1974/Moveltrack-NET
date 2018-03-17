package net.moveltrack.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.Query;

import net.moveltrack.domain.Empregado;
import net.moveltrack.domain.HouveBaixa;
import net.moveltrack.domain.Lancamento;
import net.moveltrack.domain.LancamentoStatus;
import net.moveltrack.domain.LancamentoTipo;


@Stateless
@SuppressWarnings("serial")
public class LancamentoDao extends DaoBean<Lancamento>{

	@SuppressWarnings("unchecked")
	public List<Lancamento> findByInicioFimOperadorTipo(Date inicio, Date fim, Empregado operador, LancamentoTipo operacao) {
		List<Lancamento> r = null;
		try {
			Query query = getEm().createQuery("select o from Lancamento o "
					+ "where o.data >=:inicio and o.data <=:fim "
					+ "and o.status=:status and o.solicitante.id=:operadorId and o.operacao=:operacao order by data");
			query.setParameter("inicio",inicio);
			query.setParameter("fim",fim);
			query.setParameter("operadorId", operador.getId());
			query.setParameter("status",LancamentoStatus.ATIVO);
			query.setParameter("operacao",operacao);
			r = (List<Lancamento>)query.getResultList();
		} catch (Exception e) {
			r = null;
			e.printStackTrace();
		}
		return r;
	}

	@SuppressWarnings("unchecked")
	public List<Lancamento> findByTipoInicioFim(LancamentoTipo operacao, Date inicio, Date fim) {
		try {
			Query query = getEm().createQuery("select o from Lancamento o "
					+ "where o.data >=:inicio and o.data <=:fim and o.houveBaixa=:houveBaixa "
					+ "and o.status=:status and o.operacao=:operacao order by data");
			query.setParameter("inicio",inicio);
			query.setParameter("fim",fim);
			query.setParameter("status",LancamentoStatus.ATIVO);
			query.setParameter("operacao",operacao);
			query.setParameter("houveBaixa",HouveBaixa.NAO);
			return (List<Lancamento>)query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public double sumByTipoInicioFim(LancamentoTipo operacao, Date inicio, Date fim) {
		try {
			Query query = getEm().createQuery("select SUM(o.valor) from Lancamento o "
					+ "where o.data >=:inicio and o.data <=:fim and o.houveBaixa=:houveBaixa "
					+ "and o.status=:status and o.operacao=:operacao order by data");
			query.setParameter("inicio",inicio);
			query.setParameter("fim",fim);
			query.setParameter("status",LancamentoStatus.ATIVO);
			query.setParameter("operacao",operacao);
			query.setParameter("houveBaixa",HouveBaixa.NAO);
			return (Double)query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}


}
