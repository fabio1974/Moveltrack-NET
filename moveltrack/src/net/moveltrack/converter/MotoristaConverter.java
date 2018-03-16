package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.MotoristaDao;
import net.moveltrack.domain.Motorista;

@Named
public class MotoristaConverter implements Converter, Serializable {

	public MotoristaConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	@Inject
	MotoristaDao dao;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return dao.findByNome(arg2);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Motorista) {
			Motorista obj = (Motorista) arg2;
			return obj.getNome();
		}
		return "";
	}
}