package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.EmpregadoDao;
import net.moveltrack.domain.Empregado;

@Named
public class EmpregadoConverter implements Converter, Serializable {

	public EmpregadoConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	@Inject
	EmpregadoDao dao;
	
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
		if (arg2 instanceof Empregado) {
			Empregado obj = (Empregado) arg2;
			return obj.getNome();
		}
		return "";
	}
}