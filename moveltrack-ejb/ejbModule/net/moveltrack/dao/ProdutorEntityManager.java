package net.moveltrack.dao;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.Serializable;

@SuppressWarnings("serial")
@SessionScoped
public class ProdutorEntityManager implements Serializable {
	private static EntityManagerFactory factory = Persistence.createEntityManagerFactory("moveltrack-jpa-pu");


	@Produces
	@RequestScoped
	public EntityManager getEntityManager() {
		return factory.createEntityManager();
	}

	public void finaliza(@Disposes EntityManager manager) {
		manager.close();
	}
}
