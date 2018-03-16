package net.moveltrack.dao;

import javax.ejb.Stateless;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Permissao;

@Stateless
@SuppressWarnings("serial")
public class PermissaoDao extends DaoBean<Permissao>{

	public PermissaoDao() { }
	
	public Permissao findByDescricao(String descricao) {
		Query query = getEm().createQuery("select o from Permissao o where o.descricao =:descricao");
		query.setParameter("descricao",descricao);
		try{
			return  (Permissao)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}




}
