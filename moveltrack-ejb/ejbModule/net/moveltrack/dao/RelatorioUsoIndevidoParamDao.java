package net.moveltrack.dao;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Cliente;
import net.moveltrack.domain.RelatorioUsoIndevidoParam;

@Stateless
@SuppressWarnings("serial")
public class RelatorioUsoIndevidoParamDao extends DaoBean<RelatorioUsoIndevidoParam>{

	public RelatorioUsoIndevidoParamDao() { }

	public RelatorioUsoIndevidoParam findByCliente(Cliente cliente) {
		RelatorioUsoIndevidoParam obj = null;
		Query query = getEm().createQuery("select o from RelatorioUsoIndevidoParam o where o.cliente.id ="+cliente.getId());
		try{
			obj = (RelatorioUsoIndevidoParam)query.getSingleResult();
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
		return obj;
	}




}
