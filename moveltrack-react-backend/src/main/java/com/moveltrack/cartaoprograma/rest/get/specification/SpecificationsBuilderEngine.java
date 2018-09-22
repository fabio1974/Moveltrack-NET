package com.moveltrack.cartaoprograma.rest.get.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;


public class SpecificationsBuilderEngine<T> {
     
    private final List<SearchCriteria> params;
 
    public SpecificationsBuilderEngine() {
        params = new ArrayList<SearchCriteria>();
    }
 
    public SpecificationsBuilderEngine<T> with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }
 
    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }
 
        List<Specification<T>> specs = new ArrayList<Specification<T>>();
        for (SearchCriteria param : params) {
            specs.add(new SpecificationT<T>(param));
        }
 
        Specification<T> result = specs.get(0);
        for (int i = 1; i < specs.size(); i++) {
            result = Specification.where(result).and(specs.get(i));
        }
        return result;
    }
}