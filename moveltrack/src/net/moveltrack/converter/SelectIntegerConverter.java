package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.domain.Uf;

@Named
public class SelectIntegerConverter implements Converter, Serializable {

	public SelectIntegerConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;



	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			Integer id = Integer.valueOf(arg2);
			return id.intValue();
		}catch(Exception e){
			return 0;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
			if(arg2==null)
				return "";
			return arg2.toString();
	}
}