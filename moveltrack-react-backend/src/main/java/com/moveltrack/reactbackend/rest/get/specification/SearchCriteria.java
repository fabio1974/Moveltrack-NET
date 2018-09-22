package com.moveltrack.reactbackend.rest.get.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    
	public SearchCriteria(String key2, String operation2, Object value2) {
		this.key = key2;
		this.operation = operation2;
		this.value = value2;	
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	  
    
    
    
}