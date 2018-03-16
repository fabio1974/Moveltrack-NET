package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@ManagedBean
@Named
public class ConverterHelper implements Serializable{
	
	private static final long serialVersionUID = -4364631773855223447L;

	public ConverterHelper() {
		// TODO Auto-generated constructor stub
	}
	
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@PersistenceContext( unitName="moveltrack-jpa-pu" , type=PersistenceContextType.EXTENDED)
	private EntityManager em;


}
