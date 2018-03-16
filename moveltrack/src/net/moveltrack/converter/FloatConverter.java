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
public class FloatConverter implements Converter, Serializable {

	public FloatConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			Float id = Float.valueOf(arg2);
			return id.floatValue();
		}catch(Exception e){
			return 0f;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
			return arg2.toString();
	}
}