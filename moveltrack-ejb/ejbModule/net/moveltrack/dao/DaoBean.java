package net.moveltrack.dao;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

@SuppressWarnings("serial")
public abstract class DaoBean<T> implements Serializable {

	
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager em;
    
    //private EntityManagerFactory factory = Persistence.createEntityManagerFactory("moveltrack-jpa-pu");
    //private EntityManager em = factory.createEntityManager();
	
	
	public T findById(T t) {
		return (T) em.find(getTypeClass(), t);
	}

	
	public EntityManager getEm() {
		return em;
	}


	public void setEm(EntityManager em) {
		this.em = em;
	}



	public T findById(Long id) {
		return (T) em.find(getTypeClass(), id);
	}
	
	public T findById(Integer id) {
		return (T) em.find(getTypeClass(), id);
	}


	public void refresh(T object) {
		em.refresh(object);
	}

	public void flush() {
		em.flush();
	}

	public void clear(boolean flush) {
		if (flush) flush();
		em.clear();
	}

	public T merge(T objeto) {
		objeto = em.merge(objeto);
		return objeto;
	}

	public void salvar(T objeto) {
		em.persist(objeto);
		// flush();
	}

	public void remover(T objeto) {
		em.remove(objeto);
	}

	public void remover(Integer id) {
		em.createQuery(
				"delete from " + getTypeClass().getName() + " where id = " + id)
				.executeUpdate();
	}


	public List<T> findAll() {
		return em.createQuery(" from " + getTypeClass().getName()).getResultList();
	}
	
	
	public int countAll() {
		try {
			Query query = em.createQuery("select count(o) from " + getTypeClass().getName()+ " o ");
			return (int)(long)query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
	

	
    private Class<?> getTypeClass() {
	        Class<?> clazz = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	        return clazz;
    }
	

	public List<T> findAllOrderBy(String orderBy) {
		return em.createQuery(" from " + getTypeClass().getName() +" order by " + orderBy)
				.getResultList();

	}
	
/*	public List<T> listPage(int firstRow, int rowsPerPage, String sortField, boolean sortAscending, Map<String, String> filtros) {
		String sql = " from "+ getTypeClass().getName() + buildFiltros(filtros) +" order by "+sortField+ " " +(sortAscending?"ASC":"DESC");
		Query query = em.createQuery(sql);
		query.setFirstResult(firstRow);
	    query.setMaxResults(rowsPerPage);
		return query.getResultList();
	}*/
	
	
	public List<T> listPage(int firstRow, int rowsPerPage, String sortField, boolean sortAscending, Map<String,Object> filtros) {
		String sql = " from "+ getTypeClass().getName() + buildFiltrosObject(filtros) +" order by "+sortField+ " " +(sortAscending?"ASC":"DESC");
		Query query = em.createQuery(sql);
		
		for ( String key : filtros.keySet() ) {
			if (filtros.get(key)!=null && !(filtros.get(key) instanceof String && filtros.get(key).equals(""))){
				if(filtros.get(key) instanceof String)
					query.setParameter(key.replace(".","_"),"%"+((String)filtros.get(key)).toUpperCase()+"%");
				else if(filtros.get(key) instanceof Integer)
					query.setParameter(key.replace(".","_"),filtros.get(key));
				else if(filtros.get(key) instanceof Date)
					query.setParameter(key,filtros.get(key));
				else	
					query.setParameter(key.replace(".","_"),filtros.get(key));
			}
		}	
		
		query.setFirstResult(firstRow);
	    query.setMaxResults(rowsPerPage);
		return query.getResultList();
	}
	
	

	
	
	

	
	
	public int countAll(Map<String, Object> filtros) {
		System.err.println("NOVO COUNT");
		try {
			String sql = "select count(o) from " + getTypeClass().getName() + " o " + buildFiltrosObject(filtros);
			Query query = em.createQuery(sql);
			for ( String key : filtros.keySet() ) {
				if (filtros.get(key)!=null && !(filtros.get(key) instanceof String && filtros.get(key).equals(""))){
					if(filtros.get(key) instanceof String){
						query.setParameter(key.replace(".","_"),"%"+filtros.get(key)+"%");

					}else if(filtros.get(key) instanceof Date){
						query.setParameter(key,filtros.get(key));					
					}else	
						query.setParameter(key.replace(".","_"),filtros.get(key));
				}
			}	
			int result = (int)(long)query.getSingleResult();
			return result;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return 0;
		}
	}
	
	
	
/*	public String buildFiltros(Map<String, String> filtros) {
		StringBuffer sb = new StringBuffer();
		String result = null;
		if(filtros.isEmpty())
			result =  "";
		else{
			sb.append(" where ");
			int i = 0;
			for ( String key : filtros.keySet() ) {
				if(i>=1)
					sb.append(" and ");
				sb.append("UPPER(").append(key).append(") like ").append("'%").append(filtros.get(key).toUpperCase()).append("%'");
				i++;
			}
			result =  sb.toString();
		}	
		return result;	
	}*/
	
	
	public String buildFiltrosObject(Map<String,Object> filtros) {
		StringBuffer sb = new StringBuffer();
		String result = null;
		if(filtros.keySet().size()<=0)
			result =  "";
		else{
			sb.append(" where ");
			int i = 0;
			for ( String key : filtros.keySet() ) {
				if (filtros.get(key)!=null && !(filtros.get(key) instanceof String && filtros.get(key).equals(""))){
					if(i>=1)
						sb.append(" and ");
					if(filtros.get(key) instanceof String){
						sb.append("UPPER(").append(key).append(") like :").append(key.replace(".","_"));
					}else if(filtros.get(key) instanceof Date){

						if(key.endsWith("Inicio"))  
							sb.append(key.substring(0,key.length()-6)).append(">=:").append(key);
						else if(key.endsWith("Fim"))
							sb.append(key.substring(0,key.length()-3)).append("<=:").append(key);  
					
					}else {
						if(key.endsWith("Id"))  //Refer�ncias
							sb.append(key.substring(0,key.length()-2)).append(".id=:").append(key.replace(".","_"));
						else if(key.contains("Not"))
							sb.append(key.substring(0,key.length()-4)).append("<>:").append(key);						
						else
							sb.append(key).append("=:").append(key.replace(".","_"));  //Enums, inteiros etc  
					}	
					i++;
				}
			}
			result =  sb.toString();
		}	
		return result;	
	}
	
	



}
