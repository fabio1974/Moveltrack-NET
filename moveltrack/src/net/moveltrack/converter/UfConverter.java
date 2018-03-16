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
public class UfConverter implements Converter, Serializable {

	public UfConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;

	@Inject
	ConverterHelper converterHelper;

	Class<Uf> clazz = Uf.class;

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			Integer id = Integer.valueOf(arg2);
			return converterHelper.getEm().find(clazz, id);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Uf) {
			Uf obj = (Uf) arg2;
			return obj.getId().toString();
		}
		return null;
	}
}