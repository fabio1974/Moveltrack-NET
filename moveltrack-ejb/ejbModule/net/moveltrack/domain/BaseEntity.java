package net.moveltrack.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseEntity implements Serializable{
	
	private static final long serialVersionUID = 6130852658224415884L;

	public abstract Integer getId();
	public abstract void setId(Integer id);

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (( this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BaseEntity other = (BaseEntity) obj;
		if (this.getId() == null) {
			if (other.getId() != null) {
				return false;
			}
		} else if (!this.getId().equals(other.getId())) {
			return false;
		}
		return true;
	}
	
	
	public Map<String, Object> toMap() {
		Map<String, Object> objectAsMap = new HashMap<String, Object>();
		try {
		    BeanInfo info = Introspector.getBeanInfo(this.getClass());
		    for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
		        Method reader = pd.getReadMethod();
		        if (reader != null)
		            objectAsMap.put(pd.getName(),reader.invoke(this));
		    }
		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			//when opps happens
			e.printStackTrace();
		}
	    return objectAsMap;
	}



}
