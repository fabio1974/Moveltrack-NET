package com.moveltrack.cartaoprograma.rest.get.specification;


import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class SpecificationT<T> implements Specification<T> {
 
    public SpecificationT(SearchCriteria criteria) {
		super();
		this.criteria = criteria;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SearchCriteria criteria;
 
    @Override
    public Predicate toPredicate  (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
  
        if (criteria.getOperation().equalsIgnoreCase(">=")) {
            return builder.greaterThanOrEqualTo(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());

        }else  if (criteria.getOperation().equalsIgnoreCase("<=")) {
            return builder.lessThanOrEqualTo(
                    root.<String> get(criteria.getKey()), criteria.getValue().toString());

        }else if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThan(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        } 
        
        else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThan(
              root.<String> get(criteria.getKey()), criteria.getValue().toString());
        } 
        else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(builder.upper(
                  root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toUpperCase() + "%");
            } else if (root.get(criteria.getKey()).getJavaType().getCanonicalName().contains("model")) {
            	return builder.equal(root.get(criteria.getKey()).get("id"), criteria.getValue());
        }else{
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        
        return null;
    }
}