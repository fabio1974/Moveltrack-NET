package net.moveltrack.dao;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.Query;

import net.moveltrack.domain.Perfil;
import net.moveltrack.domain.PerfilTipo;

@ApplicationScoped
@SuppressWarnings("serial")
public class PerfilDao extends DaoBean<Perfil>{

	public PerfilDao() { }

	public Perfil findByTipo(PerfilTipo tipo) {
		Query query = getEm().createQuery("select o from Perfil o where o.tipo =:tipo");
		query.setParameter("tipo",tipo);
		try{
			return  (Perfil)query.getSingleResult();
		}catch(Exception e){
			return null;
		}
	}




}
