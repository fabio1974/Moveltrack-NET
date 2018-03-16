package net.moveltrack.action;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;

import net.moveltrack.controller.action.BaseAction;
import net.moveltrack.domain.BaseEntity;

/**
 * Session Bean implementation class TableBean
 */



public abstract class TableBeanSession extends BaseAction implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6355682847375407088L;
	public TableBeanSession() {

    }
    

    
    protected int id;
    protected int totalRows;
    protected int rowsPerPage;
    protected int lastPage;
	protected int currentPage;
    protected String sortField;
    protected boolean sortAscending;
    //protected Permission permission;
    
	
    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    
    
    public int getLastPage() {
		return lastPage;
	}


	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}


	public String getNomeFiltro() {
		return nomeFiltro;
	}


	public void setNomeFiltro(String nomeFiltro) {
		this.nomeFiltro = nomeFiltro;
	}


	private String nomeFiltro;

    
	@PostConstruct
	public void init() {

	}
	

	public abstract void refreshPage();
	
	public abstract void setFilters();

    
    public void pageFirst() {
    	this.currentPage = 1;
    	//refreshFilters();
    	refreshPage();
    }
    
    public void pageNext() {
    	this.currentPage++;
    	//refreshFilters();
    	refreshPage();
    }

    public void pagePrevious() {
    	this.currentPage--;
    	//refreshFilters();
    	refreshPage();
    }

    public void pageLast() {
    	this.currentPage = lastPage;
    	//refreshFilters();
    	refreshPage();
    }

  
	public int getRowsPerPage() {
		return rowsPerPage;
	}


	public void setRowsPerPage(int rowsPerPage) {
		//refreshFilters();
		this.rowsPerPage = rowsPerPage;
	}



	public int getTotalPages() {
		return lastPage;
	}



	public void setTotalPages(int totalPages) {
		this.lastPage = totalPages;
	}


	public int getCurrentPage() {
		return currentPage;
	}


	public void loadCurrentPage(){
		//refreshFilters();
		refreshPage();
	}

	
	public void setCurrentPage(int currentPage) {
		if(currentPage>lastPage)
			currentPage = lastPage;
		if(currentPage<0)
			currentPage = 1;
		this.currentPage = currentPage;
		
	}



	public String getSortField() {
		return sortField;
	}



	public void setSortField(String sortField) {
		this.sortField = sortField;
	}



	public boolean isSortAscending() {
		return sortAscending;
	}



	public void setSortAscending(boolean sortAscending) {
		this.sortAscending = sortAscending;
	}


	public int getTotalRows() {
		return totalRows;
	}


	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}


	Map<String, Object> preFilters = new HashMap<String, Object>();
	Map<String, Object> filters = new HashMap<String, Object>();
	
	
	public void normalizeFilters() {
		filters.clear();
		for (String key : preFilters.keySet() ) {
			if(preFilters.get(key)!=null){
				if(preFilters.get(key) instanceof String){
					String value = ((String)preFilters.get(key)).trim();
					if(value.length()>0)
						filters.put(key, value);
				}else if(key.endsWith("Id")){
					if(preFilters.get(key) instanceof BaseEntity){
						BaseEntity be = (BaseEntity)preFilters.get(key);
						filters.put(key,be.getId());
					}else{
						System.out.println(key + " não é uma entidade");
					}
				}else{
					filters.put(key,preFilters.get(key));
				}
			}
		}
	}
	
	
	
	// Sorting actions ----------------------------------------------------------------------------
	public void sort(ActionEvent event) {
     String sortFieldAttribute = (String) event.getComponent().getAttributes().get("sortField");

     // If the same field is sorted, then reverse order, else sort the new field ascending.
     if (sortField.equals(sortFieldAttribute)) {
         sortAscending = !sortAscending;
     } else {
         sortField = sortFieldAttribute;
         sortAscending = true;
     }

     pageFirst(); // Go to first page and load requested page.
 }
	
	
	
/*	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}*/
	

}
