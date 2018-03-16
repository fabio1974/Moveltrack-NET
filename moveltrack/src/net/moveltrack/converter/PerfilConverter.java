package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.PerfilDao;
import net.moveltrack.domain.PerfilTipo;

@Named
public class PerfilConverter implements Converter, Serializable {

	public PerfilConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;

	@Inject
	PerfilDao dao;



	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return dao.findByTipo(PerfilTipo.valueOf(arg2));
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String r = null;
		try{
			r = dao.findById((int)arg2).getTipo().toString();
		}catch(Exception e){
			r = null;	
		}
		return r;
	}
}