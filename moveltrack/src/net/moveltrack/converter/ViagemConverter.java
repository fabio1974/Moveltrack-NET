package net.moveltrack.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;

import net.moveltrack.dao.ViagemDao;
import net.moveltrack.domain.Viagem;

@Named
public class ViagemConverter implements Converter, Serializable {

	public ViagemConverter() {
	}

	private static final long serialVersionUID = -5962685957040859192L;


	@Inject
	ViagemDao dao;
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		try{
			return dao.findByDescricao(arg2);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Viagem) {
			Viagem obj = (Viagem) arg2;
			return obj.getDescricao();
		}
		return "";
	}
}